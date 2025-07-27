package com.zizonhyunwoo.anysearch.batch.process;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zizonhyunwoo.anysearch.elastic.index.NaverData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverProcessor implements ItemProcessor<String, List<NaverData>> {

    private final ObjectMapper objectMapper;

    @Override
    public List<NaverData> process(String item) {
        try {
            JsonNode rootNode = objectMapper.readTree(item);

            List<NaverData> content = new ArrayList<>();
            JsonNode hitsArray = rootNode.path("items");

            if (hitsArray.isArray()) {
                for (JsonNode hit : hitsArray) {
                    content.add(objectMapper.treeToValue(hit, NaverData.class));
                }
            }
            return content;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
