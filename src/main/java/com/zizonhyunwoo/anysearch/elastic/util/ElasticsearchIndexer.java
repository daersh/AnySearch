package com.zizonhyunwoo.anysearch.elastic.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchIndexer {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ObjectMapper objectMapper;

    private String analysisSettingsJson; // analysis 부분만 저장할 변수
    private String mappingsJson; // mappings 부분만 저장할 변수

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("/elastic/anydataSettings.json");

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
    }



    public void saveData(String type, AnyDataDocument anyDataDocument)  {

        String dynamicIndexName = type.equals("anydata")?type:"anydata_" + type.toLowerCase(Locale.KOREA);
        IndexCoordinates indexCoordinates = getIndexCoordinates(dynamicIndexName);

        elasticsearchTemplate.save(anyDataDocument, indexCoordinates);
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

                indexSettings.put("number_of_shards", 3);
                indexSettings.put("number_of_replicas", 1);
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

    public void saveData(String type, List<AnyDataDocument> value) {
        String dynamicIndexName = type.equals("anydata")?type:"anydata_" + type.toLowerCase(Locale.KOREA);

        IndexCoordinates indexCoordinates = getIndexCoordinates(dynamicIndexName);
        elasticsearchTemplate.save(value, indexCoordinates);
    }

    // delete index - 컬렉션 제거
    public void delete(String type){
        System.out.println("type = " + type);
        type= type.startsWith("anydata")?
                type : "anydata_" + type.toLowerCase(Locale.KOREA);
        IndexCoordinates dynamicIndexCoordinates = IndexCoordinates.of(type);
        elasticsearchTemplate.indexOps(dynamicIndexCoordinates).delete();
    }

}
