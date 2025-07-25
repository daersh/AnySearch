package com.zizonhyunwoo.anysearch.batch.writer;

import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import com.zizonhyunwoo.anysearch.util.search.ElasticsearchIndexer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnyDataElasticsearchItemWriter implements ItemWriter<AnyDataDocument> {

    private final ElasticsearchIndexer elasticsearchIndexer;

    @Override
    public void write(Chunk<? extends AnyDataDocument> chunk){

        Map<String, List<AnyDataDocument>> map = new HashMap<>();
        List<AnyDataDocument> list = new ArrayList<>();
        for (AnyDataDocument anyData : chunk) {
            // 타입 초기화
            if(!map.containsKey(anyData.getType())) {
                map.put(anyData.getType(), new ArrayList<>());
            }
            // 데이터 저장
            map.get(anyData.getType().toLowerCase(Locale.KOREA)).add(anyData);
            list.add(anyData);
        }

        map.forEach(elasticsearchIndexer::saveData);
        elasticsearchIndexer.saveData("anydata", list);

    }

}
