package com.zizonhyunwoo.anysearch.elastic.util;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchFileIndexer {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ObjectMapper objectMapper;

    private String analysisSettingsJson; // analysis 부분만 저장할 변수
    private String mappingsJson; // mappings 부분만 저장할 변수
    private ClassPathResource ingestPipelineJson;
    private final ElasticsearchClient  elasticsearchClient;
    private final RestClient restClient;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("/elastic/fileData.json");

        JsonNode rootNode = objectMapper.readTree(resource.getInputStream());

        JsonNode analysisNode = rootNode.path("settings").path("index").path("analysis");
        if (!analysisNode.isMissingNode()) {
            this.analysisSettingsJson = objectMapper.writeValueAsString(analysisNode);
        } else {
            this.analysisSettingsJson = "{}";
        }

        // mappings 부분 추출
        JsonNode mappingsNode = rootNode.path("mappings");
        if (!mappingsNode.isMissingNode()) {
            this.mappingsJson = objectMapper.writeValueAsString(mappingsNode);
        } else {
            this.mappingsJson = "{}";
        }


        this.ingestPipelineJson = new ClassPathResource("/elastic/filePipeLine.json");

        createOrUpdateIngestPipeline();
    }

    private void createOrUpdateIngestPipeline() {
        try {
            String pipelineId = "file_ingest";

            JsonNode pipelineNode = objectMapper.readTree(ingestPipelineJson.getInputStream());
            String pipelineBody = objectMapper.writeValueAsString(pipelineNode);

            Request request = new Request("PUT", "/_ingest/pipeline/" + pipelineId);
            request.setJsonEntity(pipelineBody);

            Response response = restClient.performRequest(request);
            log.info("Ingest pipeline created/updated successfully: {}", pipelineId);

        } catch (Exception e) {
            log.error("Failed to create/update ingest pipeline", e);
        }
    }

    public void saveFile(String fileName, byte[] data) {
        try {
            String base64Data = Base64.getEncoder().encodeToString(data);

            String id = UUID.randomUUID().toString();

            Map<String, Object> doc = new HashMap<>();
            doc.put("id", id);
            doc.put("filename", fileName);
            doc.put("document_binary_data", base64Data);
            doc.put("uploadedAt", Instant.now().toString());

            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                    .index("anydata_file")
                    .id(id)
                    .document(doc)
                    .pipeline("file_ingest")
            );

            IndexResponse response = elasticsearchClient.index(request);

            log.info("Indexed file '{}' with ID '{}'", fileName, response.id());

        } catch (Exception e) {
            log.error("Failed to index file '{}'", fileName, e);
        }
    }

    public IndexCoordinates getIndexCoordinates(String type) {

        IndexCoordinates dynamicIndexCoordinates = IndexCoordinates.of(type);
        IndexOperations dynamicIndexOperations = elasticsearchTemplate.indexOps(dynamicIndexCoordinates);

        if (!dynamicIndexOperations.exists()) {
            try {
                log.info("Creating dynamic index: {}", type);

                Map<String, Object> settingsMap = new HashMap<>();
                Map<String, Object> indexSettings = new HashMap<>();
                Map<String, Object> analysisSettings = objectMapper.readValue(this.analysisSettingsJson, Map.class);

                indexSettings.put("number_of_shards", 1);
                indexSettings.put("number_of_replicas", 0);
                indexSettings.put("analysis", analysisSettings);
                settingsMap.put("index", indexSettings);

                Document mappingDocument = Document.parse(this.mappingsJson);

                dynamicIndexOperations.create(settingsMap, mappingDocument);

            }catch (Exception e){
                log.error(e.getMessage());
            }
        }
        return dynamicIndexCoordinates;
    }

}
