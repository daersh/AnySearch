package com.zizonhyunwoo.anysearch.batch.writer;

import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import com.zizonhyunwoo.anysearch.util.ElasticsearchUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnyDataElasticsearchItemWriter implements ItemWriter<AnyDataDocument> {

    private final ElasticsearchUtil elasticsearchUtil;

    @Override
    public void write(Chunk<? extends AnyDataDocument> chunk){
        for (AnyDataDocument anyData : chunk) {
            elasticsearchUtil.saveData(anyData.getType(),anyData);
            elasticsearchUtil.saveData("anydata",anyData);
        }
    }
}
