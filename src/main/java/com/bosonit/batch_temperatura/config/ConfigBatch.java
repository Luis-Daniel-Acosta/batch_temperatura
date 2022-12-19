package com.bosonit.batch_temperatura.config;

import com.bosonit.batch_temperatura.job.WeatherItemReader;
import com.bosonit.batch_temperatura.job.WeatherItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableBatchProcessing
public class ConfigBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public WeatherItemReader reader(){
        return new WeatherItemReader();
    }

    @Bean
    public WeatherItemWriter writerRead(){
        return new WeatherItemWriter();
    }

    @Value("classpath*:/input/data*.csv")
    private Resource[] inputResources;


    @Bean
    public Job readCSVTemperature(){
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();

    }

    private Step step1() {
        return stepBuilderFactory.get("step1")
                .chunk(100)
                .reader(reader()).writer()
    }


}
