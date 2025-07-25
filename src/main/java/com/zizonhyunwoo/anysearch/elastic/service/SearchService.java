package com.zizonhyunwoo.anysearch.elastic.service;


import com.zizonhyunwoo.anysearch.elastic.response.SearchResponse;
import org.springframework.data.elasticsearch.core.IndexInformation;

import java.util.List;

public interface SearchService {

    SearchResponse search(String request, Integer page, Integer size, String type);

    List<IndexInformation> findAllIndexes();

    List<String> getAutoCompletion(String query, String type);
}
