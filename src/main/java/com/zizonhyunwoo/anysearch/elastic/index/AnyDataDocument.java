package com.zizonhyunwoo.anysearch.elastic.index;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "anydata")
@Setting(settingPath = "/elastic/anydataSettings.json")
public class AnyDataDocument {

    @Id
    private String id;
    @Field(type = FieldType.Keyword)
    private String type;
    @Field(type = FieldType.Text, analyzer = "korean_tokenizer_advanced_analyzer")
    private String title;
    @Field(type = FieldType.Text, analyzer = "korean_tokenizer_advanced_analyzer")
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
