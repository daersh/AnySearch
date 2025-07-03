package com.zizonhyunwoo.anysearch.elastic.dao;

import com.zizonhyunwoo.anysearch.elastic.index.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ElasticsearchRepository<Product, String> {

}
