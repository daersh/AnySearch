package com.zizonhyunwoo.anysearch.elastic.index;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.suggest.Completion;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "index_sample")
public class IndexSample {

    @Id
    private String id;

    @Field(type = FieldType.Long, index = false)
    private Long longfield;

    @Field(type = FieldType.Text, analyzer = "my_custom_analyzer")
    private String textfield;

    @Field(type = FieldType.Date, index = false, pattern = "yyyy-MM-dd")
    private LocalDate datefield;

    @Field(type = FieldType.Keyword, index = false)
    private String keywordfield;

    @Field(type = FieldType.Boolean)
    private Boolean booleanfield;

    @Field(type = FieldType.Float)
    private Float floatfield;

    @GeoPointField
    private GeoPoint location;

    @CompletionField
    private Completion suggestfield;

    @Field(type = FieldType.Nested)
    private List<Tag> tags;

    @Field(type = FieldType.Ip)
    private String ip_address;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tag {
        @Field(type = FieldType.Keyword)
        private String name;

        // value: 엘라스틱서치 매핑이 "type": "text"이므로 String 타입이 올바릅니다.
        @Field(type = FieldType.Text)
        private String value;
    }

    @Field(type = FieldType.Text, analyzer = "my_autocomplete_analyzer")
    private String autocompleteField;
}
