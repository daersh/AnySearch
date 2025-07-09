package com.zizonhyunwoo.anysearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/batch")
public class JobController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @GetMapping("/any_data")
    public String anyData() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", LocalDateTime.now().toString())
                .toJobParameters();
        try {
            jobLauncher.run(jobRegistry.getJob("anyDataToElasticsearchJob"),jobParameters);

        }catch (Exception e){
            log.error(e.getMessage());
        }
        return "success";
    }
}
