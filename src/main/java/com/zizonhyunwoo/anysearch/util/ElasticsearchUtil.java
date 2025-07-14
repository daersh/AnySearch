package com.zizonhyunwoo.anysearch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
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
public class ElasticsearchUtil {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ObjectMapper objectMapper;

    private String analysisSettingsJson; // analysis 부분만 저장할 변수
    private String mappingsJson; // mappings 부분만 저장할 변수

    @PostConstruct
    public void init() throws IOException {
        // anydataSettings.json 파일에서 settings와 mappings를 분리하여 로드
        ClassPathResource resource = new ClassPathResource("/elastic/anydataSettings.json");
        JsonNode rootNode = objectMapper.readTree(resource.getInputStream());

        // settings.index.analysis 부분만 추출
        JsonNode analysisNode = rootNode.path("settings").path("index").path("analysis");
        if (!analysisNode.isMissingNode()) {
            this.analysisSettingsJson = objectMapper.writeValueAsString(analysisNode);
        } else {
            log.warn("anydataSettings.json does not contain 'settings.index.analysis' block.");
            this.analysisSettingsJson = "{}"; // 빈 객체로 초기화
        }

        // mappings 부분 추출
        JsonNode mappingsNode = rootNode.path("mappings");
        if (!mappingsNode.isMissingNode()) {
            this.mappingsJson = objectMapper.writeValueAsString(mappingsNode);
        } else {
            log.warn("anydataSettings.json does not contain 'mappings' block. Auto-mapping will be used.");
            this.mappingsJson = "{}"; // 빈 객체로 초기화
        }
    }

    public Map<String, String> parseData(String addInfo, String addDetail) {

        String delimiter = "†";

        int count = addInfo.split(delimiter).length;
        List<String> keys = Arrays.asList(addInfo.split(delimiter));
        List<String> values = Arrays.asList(addDetail.split(delimiter));
        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < count; i++) {
            try {
                data.put(keys.get(i), values.get(i));
            }catch (Exception e){
                log.error(e.getMessage());
                break;
            }
        }
        return data;
    }

    public void saveData(String type, AnyDataDocument anyDataDocument)  {
        String dynamicIndexName = type.equals("anydata")?type:"anydata_" + type.toLowerCase(Locale.KOREA);

        IndexCoordinates dynamicIndexCoordinates = IndexCoordinates.of(dynamicIndexName);
        IndexOperations dynamicIndexOperations = elasticsearchTemplate.indexOps(dynamicIndexCoordinates);

        if (!dynamicIndexOperations.exists()) {
            try {
                log.info("Creating dynamic index: {}", dynamicIndexName);

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

        elasticsearchTemplate.save(anyDataDocument,dynamicIndexCoordinates);
    }
}
