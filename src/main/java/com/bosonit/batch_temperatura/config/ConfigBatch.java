package com.bosonit.batch_temperatura.config;

import com.bosonit.batch_temperatura.domain.Weather;
import com.bosonit.batch_temperatura.domain.WeatherRisk;
import com.bosonit.batch_temperatura.job.WeatherItemProcessor;
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

import java.nio.file.Watchable;
import java.util.function.Function;

@Configuration
@EnableBatchProcessing
public class ConfigBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public WeatherItemReader reader(){return new WeatherItemReader();
    }
    @Bean
    public WeatherItemWriter writerRead(){return new WeatherItemWriter();
    }

    @Bean
    public WeatherItemProcessor processor() {
        return new WeatherItemProcessor();
    }

    @Value("classpath*:/input/data*.csv")
    private Resource[] inputResources;


    @Bean
    public Job readCSVTemperature(Step step){
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();

    }
    @Bean
    public Step step(WeatherItemReader reader,WeatherItemWriter writer/*, WeatherItemProcessor processor*/) {
        return stepBuilderFactory.get("step1")
                .<Weather, WeatherRisk>chunk(100)
                .reader(reader)
//                .processor(processor)
                .writer(writer)
                .build();
    }


}
