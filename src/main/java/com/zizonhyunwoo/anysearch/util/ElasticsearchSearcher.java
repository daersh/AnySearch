package com.zizonhyunwoo.anysearch.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexInformation;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchSearcher {
    private final ElasticsearchOperations elasticsearchOperations;

    public String createMultiMatchQuery(String query, List<String> fields, String analyzer) {
        System.out.println("fields = " + fields);
        String test= """
                {
              "multi_match": {
                "query": "%s",
                "fields": %s,
                "analyzer": "%s"
              }
            }
        """.formatted(query, "[\""+String.join("\",\"",fields) +"\"]", analyzer);
        System.out.println("test = " + test);
        return test;
    }

    public <T> SearchHits<T> search(Query query, String type, Class<T> clazz) {
        return elasticsearchOperations.search(query, clazz, IndexCoordinates.of(type));
    }

    public List<IndexInformation> getAllIndexInfo() {
        IndexCoordinates allIndices = IndexCoordinates.of("*");
        IndexOperations indexOperations = elasticsearchOperations.indexOps(allIndices);

        return indexOperations.getInformation(allIndices).stream()
                .filter(data->!data.getName().startsWith("."))
                .collect(Collectors.toList());
    }


}
