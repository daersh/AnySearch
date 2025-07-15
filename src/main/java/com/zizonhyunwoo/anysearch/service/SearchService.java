package com.zizonhyunwoo.anysearch.service;


import com.zizonhyunwoo.anysearch.controller.SearchResponse;
import org.springframework.data.elasticsearch.core.IndexInformation;

import java.util.List;

public interface SearchService {

    SearchResponse search(String request, Integer page, Integer size, String type);

    List<IndexInformation> findAllIndexes();
}
