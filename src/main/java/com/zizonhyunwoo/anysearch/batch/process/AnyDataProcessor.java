package com.zizonhyunwoo.anysearch.batch.process;

import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import com.zizonhyunwoo.anysearch.util.ParsingUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnyDataProcessor implements ItemProcessor<AnyData, AnyDataDocument> {

    @Override
    public AnyDataDocument process(AnyData item) {

        Map<String,String> addFieldList = ParsingUtil.parseData(item.getAddInfo(),item.getAddDetail());

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
                .autoCompletion(item.getTitle())
                .build();
    }

}
