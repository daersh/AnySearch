package com.zizonhyunwoo.anysearch.batch.writer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document; // Document 임포트 추가
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap; // HashMap 임포트 추가
import java.util.Locale;
import java.util.Map; // Map 임포트 추가

@Component
@Slf4j
@RequiredArgsConstructor
public class AnyDataElasticsearchItemWriter implements ItemWriter<AnyDataDocument> {

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

    @Override
    public void write(Chunk<? extends AnyDataDocument> chunk) throws Exception {
        for (AnyDataDocument anyData : chunk) {
            String dynamicIndexName = "anydata_" + anyData.getType().toLowerCase(Locale.KOREA);

            IndexCoordinates dynamicIndexCoordinates = IndexCoordinates.of(dynamicIndexName);
            IndexOperations dynamicIndexOperations = elasticsearchTemplate.indexOps(dynamicIndexCoordinates);

            if (!dynamicIndexOperations.exists()) {
                log.info("Creating dynamic index: {}", dynamicIndexName);

                // 인덱스 생성 요청 JSON을 Map<String, Object> 형태로 구성
                Map<String, Object> settingsMap = new HashMap<>();
                Map<String, Object> indexSettings = new HashMap<>();
                Map<String, Object> analysisSettings = objectMapper.readValue(this.analysisSettingsJson, Map.class);

                indexSettings.put("number_of_shards", 1); // 샤드 수 고정
                indexSettings.put("number_of_replicas", 0); // 레플리카 수 고정
                indexSettings.put("analysis", analysisSettings); // analysis 설정 추가
                settingsMap.put("index", indexSettings);

                Document mappingDocument = Document.parse(this.mappingsJson);

                dynamicIndexOperations.create(settingsMap, mappingDocument);

            }
            elasticsearchTemplate.save(anyData, dynamicIndexCoordinates);

            // "anydata" 메인 인덱스는 @Setting 어노테이션에 의해 Spring Data Elasticsearch가 처리하므로,
            // 이 부분은 보통 제거하는 것이 좋습니다.
            // IndexCoordinates mainIndexCoordinates = IndexCoordinates.of("anydata");
            // IndexOperations mainIndexOperations = elasticsearchTemplate.indexOps(mainIndexCoordinates);
            // if (!mainIndexOperations.exists()) {
            //     log.info("Creating main index: {}", "anydata");
            // }
            // elasticsearchTemplate.save(anyData, mainIndexCoordinates);
        }
    }
}
