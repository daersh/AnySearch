package com.zizonhyunwoo.anysearch.elastic.dao;

import com.zizonhyunwoo.anysearch.elastic.index.IndexSample;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IndexSampleRepository extends ElasticsearchRepository<IndexSample, String> {
    @Query("""
            {
                "match" :{
                    "textfield":{
                        "query": "?0",
                        "analyzer": "my_custom_analyzer"
                    }
                }
            }
            """)
    Iterable<IndexSample> searchTextFieldByAnalyzer(String keyword);

    @Query("""
        {
          "multi_match": {
            "query": "?0",
            "fields": ["textfield", "tags.value"],
            "analyzer": "my_custom_analyzer" 
          }
        }
        """)
    Slice<IndexSample> searchAllFieldsWithAnalyzer(String keyword);
}
