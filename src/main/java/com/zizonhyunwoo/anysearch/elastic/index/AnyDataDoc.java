package com.zizonhyunwoo.anysearch.elastic.index;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@Document(indexName = "anydata_board")
public class AnyDataDoc {

    @Id
    private UUID id;

    @Field(type = FieldType.Text, fielddata = true)
    private String title;

    @Field(type = FieldType.Text, fielddata = true)
    private String description;

    @Field(type = FieldType.Text, fielddata = true)
    private String type;
//
//    @Field(type = FieldType.Keyword)
//    private Boolean isActive;
//
//    @Field(type = FieldType.Text)
//    private String userId;
//
//    @Field(type = FieldType.Date)
//    private LocalDateTime createdAt;
//    @Field(type = FieldType.Date)
//    private LocalDateTime updatedAt;
//
    @Field(type = FieldType.Object)
    private Map<String, String> additionalFields;

}