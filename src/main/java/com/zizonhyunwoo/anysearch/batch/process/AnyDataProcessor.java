package com.zizonhyunwoo.anysearch.batch.process;

import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AnyDataProcessor implements ItemProcessor<AnyData, AnyDataDocument> {

    @Override
    public AnyDataDocument process(AnyData item) {

        Map<String,String> addFieldList = parseData(item.getAddInfo(),item.getAddDetail());

        return AnyDataDocument.builder()
                .id(item.getId().toString())
                .type(item.getType())
                .title(item.getTitle())
                .description(item.getDescription())
                .additionalFields(addFieldList)
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .isActive(item.getIsActive())
                .userId(item.getUserInfo().getEmail())
                .build();
    }

    private Map<String, String> parseData(String addInfo, String addDetail) {

        String delimiter = "†";

        int count = addInfo.split(delimiter).length;
        List<String> keys = Arrays.asList(addInfo.split(delimiter));
        List<String> values = Arrays.asList(addDetail.split(delimiter));
        Map<String, String> data = new HashMap<>();
        for (int i = 0; i < count; i++) {
            try {
                data.put(keys.get(i), values.get(i));
            }catch (Exception e){
                log.error(e.getMessage());
                break;
            }
        }
        return data;
    }
}
