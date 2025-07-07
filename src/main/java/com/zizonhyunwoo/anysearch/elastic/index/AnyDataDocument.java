package com.zizonhyunwoo.anysearch.elastic.index;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Dynamic;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "anydata")
public class AnyDataDocument {

    @Id
    private String id;
    @Field(type = FieldType.Keyword)
    private String type;
    @Field(type = FieldType.Text, analyzer = "my_custom_analyzer")
    private String title;
    @Field(type = FieldType.Text, analyzer = "my_custom_analyzer")
    private String description;
    @Field(type = FieldType.Object, dynamic = Dynamic.TRUE)
    private Map<String, String> additionalFields;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;
    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;

    @Field(type = FieldType.Boolean)
    private Boolean isActive;

    @Field(type = FieldType.Keyword)
    private String userId;
}
