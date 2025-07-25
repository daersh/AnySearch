package com.zizonhyunwoo.anysearch.elastic.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zizonhyunwoo.anysearch.elastic.response.SearchResponse;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataFile;
import com.zizonhyunwoo.anysearch.elastic.util.ElasticsearchSearcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    @Value("${spring.elasticsearch.uris}")
    private String uri;
    private final ObjectMapper objectMapper;
    private final ElasticsearchSearcher elasticsearchSearcher;

    @Override
    public SearchResponse search(String request, Integer page, Integer size, String type) {
        if (request == null || request.isBlank()) {
            return searchAll(page,size,type);
        }
        Query query;
        if(type.equals("anydata_file")){
            return searchFile(request,page,size,type);
        }else {
            query = new StringQuery(elasticsearchSearcher.createMultiMatchQuery(
                    request,
                    List.of("title", "description", "additionalFields.*"),
                    "korean_tokenizer_advanced_analyzer"));
        }
        query.setPageable(PageRequest.of(page,size));
        try {

            SearchHits<AnyDataDocument> hits = elasticsearchSearcher.search(query,type, AnyDataDocument.class);
            Long totalHits = hits.getTotalHits();
            List<AnyDataDocument> content = hits.stream().map(SearchHit::getContent).toList();

            return new SearchResponse(totalHits,content);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    private SearchResponse searchFile(String request, Integer page, Integer size, String type) {
        Query query= new StringQuery(elasticsearchSearcher.createMultiMatchQuery(
                request,
                List.of("filename","attachment.content"),
                "korean_tokenizer_advanced_analyzer"));
        query.setPageable(PageRequest.of(page,size));
        try {

            SearchHits<AnyDataFile> hits = elasticsearchSearcher.search(query,type, AnyDataFile.class);
            Long totalHits = hits.getTotalHits();
            List<AnyDataFile> content = hits.stream().map(SearchHit::getContent).toList();


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
            if(type.equals("anydata_file")){
                List<AnyDataFile> content = new ArrayList<>();
                JsonNode hitsArray = rootNode.path("hits").path("hits");
                if (hitsArray.isArray()) {
                    for (JsonNode hit : hitsArray) {
                        content.add(objectMapper.treeToValue(hit.path("_source"), AnyDataFile.class));
                    }
                }

                return new SearchResponse(totalHits, content);
            }
            List<AnyDataDocument> content = new ArrayList<>();
            JsonNode hitsArray = rootNode.path("hits").path("hits");
            if (hitsArray.isArray()) {
                for (JsonNode hit : hitsArray) {
                    content.add(objectMapper.treeToValue(hit.path("_source"), AnyDataDocument.class));
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

        return elasticsearchSearcher.getAllIndexInfo();
    }


    @Override
    public List<String> getAutoCompletion(String request, String type) {
        Query query = new StringQuery(elasticsearchSearcher.createAutoCompleteQuery(request));

        query.setPageable(PageRequest.of(0,5));
        try {
            SearchHits<AnyDataDocument> hits = elasticsearchSearcher.getAutoCompletion(query,type, AnyDataDocument.class);
            Long totalHits = hits.getTotalHits();
            return hits.stream()
                    .map(SearchHit::getContent)
                    .toList()
                    .subList(0,totalHits<5?Integer.parseInt(String.valueOf(totalHits)):5)
                    .stream()
                    .map(AnyDataDocument::getTitle)
                    .toList();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
