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
@Document(indexName = "anydata_file")
@Setting(settingPath = "/elastic/fileData.json")
public class AnyDataFile {

    @Id
    private String id;

    private String filename;

    private String data;

    private String uploadedAt;

    @Field(type = FieldType.Object)
    private Map<String, Object> attachment;
}
