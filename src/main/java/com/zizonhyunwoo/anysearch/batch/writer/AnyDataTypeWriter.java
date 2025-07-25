package com.zizonhyunwoo.anysearch.batch.writer;

import com.zizonhyunwoo.anysearch.elastic.util.ElasticsearchIndexer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnyDataTypeWriter implements ItemWriter<String> {

    private final ElasticsearchIndexer indexer;

    @Override
    public void write(Chunk<? extends String> chunk) {
        indexer.delete("anydata");
        for (String type : chunk) {
            indexer.delete(type);
        }
    }
}
