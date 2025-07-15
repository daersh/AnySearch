package com.zizonhyunwoo.anysearch.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zizonhyunwoo.anysearch.controller.SearchResponse;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDoc;
import com.zizonhyunwoo.anysearch.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    @Value("${spring.elasticsearch.uris}")
    private String uri;
    private final ObjectMapper objectMapper;

    @Override
    public SearchResponse search(String request, Integer page, Integer size, String type) {
        if (request == null || request.isBlank()) {
            return searchAll(page,size,type);
        }
        String jsonQuery = """
            {
              "multi_match": {
                "query": "%s",
                "fields": ["title", "description", "additionalFields.*"],
                "analyzer": "korean_tokenizer_advanced_analyzer"
              }
            }
            """.formatted(request.replace("\"", "\\\""));

        Query query = new StringQuery(jsonQuery);
        query.setPageable(PageRequest.of(page,size));
        try {
            SearchHits<AnyDataDoc> hits = elasticsearchOperations.search(query, AnyDataDoc.class, IndexCoordinates.of(type));

            Long totalHits = hits.getTotalHits();
            List<AnyDataDoc> content = hits.stream().map(SearchHit::getContent).toList();

            return new SearchResponse(totalHits,content);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }


    private SearchResponse searchAll(Integer page, Integer size, String type) {

        StringBuilder request = new StringBuilder()
                .append(uri).append("/").append(type).append("/_search");

        String body=  """
            {
              "size": %s,
              "from": %s
            }
            """.formatted(size,page*size);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        try {
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.postForObject(request.toString(), requestEntity,String.class);
            JsonNode rootNode = objectMapper.readTree(result);

            Long totalHits = rootNode.path("hits").path("total").path("value").asLong();

            List<AnyDataDoc> content = new ArrayList<>();
            JsonNode hitsArray = rootNode.path("hits").path("hits");
            if (hitsArray.isArray()) {
                for (JsonNode hit : hitsArray) {
                    content.add(objectMapper.treeToValue(hit.path("_source"), AnyDataDoc.class));
                }
            }

            return new SearchResponse(totalHits, content);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // 모든 컬렉션 로드
    @Override
    public List<IndexInformation> findAllIndexes() {

        IndexCoordinates allIndices = IndexCoordinates.of("*");
        IndexOperations indexOperations = elasticsearchOperations.indexOps(allIndices);

        return indexOperations.getInformation(allIndices).stream()
                .filter(data->!data.getName().startsWith("."))
                .collect(Collectors.toList());
    }

    @Override
    public String test() {
        return "";
    }

}
