package com.zizonhyunwoo.anysearch.batch.writer;

import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnyDataElasticsearchItemWriter implements ItemWriter<AnyDataDocument> {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void write(Chunk<? extends AnyDataDocument> chunk) throws Exception {
        for (AnyDataDocument anyData : chunk) {
            String indexName = "anydata_"+anyData.getType().toLowerCase(Locale.KOREA);

            IndexCoordinates indexCoordinates = IndexCoordinates.of(indexName);
            IndexOperations indexOperations = elasticsearchTemplate.indexOps(indexCoordinates);
            if (!indexOperations.exists())
                indexOperations.create();


            elasticsearchTemplate.save(anyData, indexCoordinates);

            IndexCoordinates indexCoordinates2 = IndexCoordinates.of("anydata");
            IndexOperations indexOperations2 = elasticsearchTemplate.indexOps(indexCoordinates2);
            if (!indexOperations2.exists())
                indexOperations2.create();

            elasticsearchTemplate.save(anyData, indexCoordinates);

        }
    }
}
