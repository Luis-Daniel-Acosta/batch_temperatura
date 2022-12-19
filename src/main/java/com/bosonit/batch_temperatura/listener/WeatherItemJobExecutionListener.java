package com.bosonit.batch_temperatura.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;



@Log4j2
@Component
public class WeatherItemJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("beforeJob");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("afterJob: " + jobExecution.getStatus());
    }
}
