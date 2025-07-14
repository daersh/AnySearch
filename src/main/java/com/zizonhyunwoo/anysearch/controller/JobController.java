package com.zizonhyunwoo.anysearch.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/batch")
public class JobController {

    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;
    private final JobExplorer jobExplorer;
    private static final String JOB_NAME = "anyDataToElasticsearchJob";

    @GetMapping("/any_data")
    @Scheduled(cron = "0 * * * * *")
    public String anyData() {

        Set<JobExecution> runningJobs = jobExplorer.findRunningJobExecutions(JOB_NAME);

        if (!runningJobs.isEmpty())
            return "already job started";

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("datetime", LocalDateTime.now().toString())
                .toJobParameters();

        try {
            log.info("Attempting to launch job '{}' with parameters: {}", JOB_NAME, jobParameters);
            jobLauncher.run(jobRegistry.getJob(JOB_NAME), jobParameters);

        } catch (Exception e) {
            return "error: " + e.getMessage();
        }

        return "success";
    }

    @GetMapping("/any_data/health")
    public String anyDataHealth() {
        Set<JobExecution> runningJobs = jobExplorer.findRunningJobExecutions(JOB_NAME);

        if (!runningJobs.isEmpty()) {
            return "already job started";
        }
        return "ready";
    }
}
