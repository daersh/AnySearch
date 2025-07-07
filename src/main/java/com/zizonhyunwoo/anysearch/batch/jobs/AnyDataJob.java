package com.zizonhyunwoo.anysearch.batch.jobs;


import com.zizonhyunwoo.anysearch.batch.process.AnyDataProcessor;
import com.zizonhyunwoo.anysearch.batch.writer.AnyDataElasticsearchItemWriter;
import com.zizonhyunwoo.anysearch.dao.AnyDataRepository;
import com.zizonhyunwoo.anysearch.domain.AnyData;
import com.zizonhyunwoo.anysearch.elastic.index.AnyDataDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class AnyDataJob {

    private final JobRepository jobRepository;
    private final AnyDataRepository anyDataRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final AnyDataProcessor anyDataProcessor;
    private final AnyDataElasticsearchItemWriter anyDataElasticsearchItemWriter;

    @Bean
    public Job anyDataToElasticsearchJob(){
        return new JobBuilder("anyDataToElasticsearchJob",jobRepository)
                .start(transferAnyDataToElasticsearchStep())
                .build();
    }

    private Step transferAnyDataToElasticsearchStep() {
        return new StepBuilder("transferAnyDataToElasticsearchStep", jobRepository)
                .<AnyData, AnyDataDocument>chunk(
                        10,platformTransactionManager
                )
                .reader(anyDataReader())
                .processor(anyDataProcessor)
                .writer(anyDataElasticsearchItemWriter)
                .build();
    }

    private ItemReader<AnyData> anyDataReader() {
        return new RepositoryItemReaderBuilder<AnyData>()
                .name("anyDataReader")
                .repository(anyDataRepository)
                .methodName("findAll")
                .pageSize(10)
                .sorts(Map.of("createdAt", Sort.Direction.ASC))
                .build();
    }
    
}
