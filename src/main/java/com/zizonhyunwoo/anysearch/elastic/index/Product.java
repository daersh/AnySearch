package com.zizonhyunwoo.anysearch.elastic.index;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private String price;
    private String image;

}
