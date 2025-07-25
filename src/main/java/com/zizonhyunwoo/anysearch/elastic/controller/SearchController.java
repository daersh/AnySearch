package com.zizonhyunwoo.anysearch.elastic.controller;

import com.zizonhyunwoo.anysearch.elastic.response.SearchResponse;
import com.zizonhyunwoo.anysearch.elastic.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/analyzer2")
    public SearchResponse getAnalyzerIndexSample2(
            @RequestParam String request,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String type
    ) {

        return searchService.search(request, page,size,type);
    }

    @GetMapping("/dashboard")
    public List<IndexInformation> getAnalyzerIndexSample(){
        return searchService.findAllIndexes();
    }

    @GetMapping("/auto_completion")
    public ResponseEntity<List<String>> autoCompletion( @RequestParam String query, @RequestParam String type){
        return ResponseEntity.ok(searchService.getAutoCompletion(query, type));
    }
}
