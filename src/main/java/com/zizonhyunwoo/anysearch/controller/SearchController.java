package com.zizonhyunwoo.anysearch.controller;

import com.zizonhyunwoo.anysearch.elastic.dao.IndexSampleRepository;
import com.zizonhyunwoo.anysearch.elastic.dao.ProductRepository;
import com.zizonhyunwoo.anysearch.elastic.index.IndexSample;
import com.zizonhyunwoo.anysearch.elastic.index.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

    private final ProductRepository productRepository;
    private final IndexSampleRepository indexSampleRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @GetMapping
    public Iterable<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/sample")
    public Iterable<IndexSample> getSampleProducts() {
        System.out.println("getSampleProducts");
        try {
            System.out.println(indexSampleRepository.findAll());

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return indexSampleRepository.findAll();
    }

    @GetMapping("/analyzer")
    public Iterable<IndexSample> getAnalzerIndexSample(
            @RequestParam String sample,
            @RequestParam Integer page,
            @RequestParam Integer size) {

        try {
            log.info("search: {},{},{}", sample, page, size);
            PageRequest pageRequest = PageRequest.of(page,size);
            return indexSampleRepository.searchTextFieldByAnalyzer(sample);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping("/anlyzer2")
    public Iterable<IndexSample> getAnalzerIndexSample2(
            @RequestParam String request,
            @RequestParam Integer page,
            @RequestParam Integer size) {

        String jsonQuery = """
            {
              "match": {
                "textfield": {
                  "query": "%s",
                  "analyzer": "my_custom_analyzer"
                }
              }
            }
            """.formatted(request.replace("\"", "\\\""));

        Query query = new StringQuery(jsonQuery);
        query.setPageable(PageRequest.of(page,size));

        SearchHits<IndexSample> hits = elasticsearchOperations.search(query, IndexSample.class);

        List<IndexSample> content = hits.stream().map(SearchHit::getContent).toList();

        return content;
    }
}
