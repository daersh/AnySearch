package com.zizonhyunwoo.anysearch.batch.jobs;


import com.zizonhyunwoo.anysearch.batch.process.NaverProcessor;
import com.zizonhyunwoo.anysearch.elastic.index.NaverData;
import com.zizonhyunwoo.anysearch.batch.reader.NaverReader;
import com.zizonhyunwoo.anysearch.batch.writer.NaverWriter;
import com.zizonhyunwoo.anysearch.elastic.util.ElasticsearchIndexer;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class NaverShopJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final NaverReader naverReader;
    private final NaverProcessor naverProcessor;
    private final NaverWriter naverWriter;
    private final ElasticsearchIndexer elasticsearchIndexer;

    @Bean
    public Job naverJob(){
        elasticsearchIndexer.delete("naver");

        return new JobBuilder("naverJob",jobRepository)
                .start(getNaverDatas())
                .build();
    }

    private Step getNaverDatas() {
        return new StepBuilder("getNaverDatasStep", jobRepository)
                .<String, List<NaverData>>chunk(
                        1,platformTransactionManager
                )
                .reader(naverReader)
                .processor(naverProcessor)
                .writer(naverWriter)
                .build();
    }

}
