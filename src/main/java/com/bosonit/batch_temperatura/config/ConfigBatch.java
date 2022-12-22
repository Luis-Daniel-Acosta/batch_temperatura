package com.bosonit.batch_temperatura.config;

import com.bosonit.batch_temperatura.domain.weather.Weather;
import com.bosonit.batch_temperatura.domain.weather.WeatherDto;
import com.bosonit.batch_temperatura.domain.weatherRisk.WeatherRisk;
import com.bosonit.batch_temperatura.domain.weatherRisk.WeatherRiskDto;
import com.bosonit.batch_temperatura.job.step1.WeatherItemProcessor;
import com.bosonit.batch_temperatura.job.WeatherItemReader;
import com.bosonit.batch_temperatura.job.step1.WeatherItemWriter;
import com.bosonit.batch_temperatura.job.step1.WeatherReaderStep1;
import com.bosonit.batch_temperatura.job.step2.WeatherWriteStep2;
import com.bosonit.batch_temperatura.job.step2.weaterItemProcessorStep2;
import com.bosonit.batch_temperatura.job.step3.WeatherItemProcessorStep3;
import com.bosonit.batch_temperatura.job.step3.WeatherReaderStep3;
import com.bosonit.batch_temperatura.job.step3.WeatherRiskItemWriteStep3;
import com.bosonit.batch_temperatura.job.step4.WeaterItemProcessorStep4;
import com.bosonit.batch_temperatura.job.step4.WeatherReaderStep4;
import com.bosonit.batch_temperatura.job.step4.WeatherWriteStep4;
import com.bosonit.batch_temperatura.listener.WeatherItemProcessorListener;
import com.bosonit.batch_temperatura.listener.WeatherItemReaderListener;
import com.bosonit.batch_temperatura.listener.WeatherItemWriterListener;
import com.bosonit.batch_temperatura.repository.RepositoryWeather;
import com.bosonit.batch_temperatura.repository.RespositoryWeatherRisk;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;


@Configuration
@EnableBatchProcessing
public class ConfigBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    DataSource dataSource;

    @Autowired
    RepositoryWeather repositoryWeather;

    @Autowired
    RespositoryWeatherRisk respositoryWeatherRisk;

    @Bean
    public WeatherItemReader weatherItemReader(){return new WeatherItemReader();
    }
    @Bean
    public WeatherItemWriter weatherItemWriter(){return new WeatherItemWriter();
    }
    @Bean
    public WeatherItemProcessor weatherItemProcessor() {
        return new WeatherItemProcessor();
    }
    @Bean
    public WeatherItemReaderListener weatherItemReaderListener(){return new WeatherItemReaderListener();}
    @Bean
    public WeatherItemProcessorListener weatherItemProcessorListener(){return new WeatherItemProcessorListener();}
    @Bean
    public WeatherItemWriterListener weatherItemWriterListener(){return new WeatherItemWriterListener();}

    @Bean
    public WeatherRiskItemWriteStep3 weatherRiskItemWriteStep3(){return new WeatherRiskItemWriteStep3();}

    @Bean
    public WeatherReaderStep1 weatherReaderStep1(){return new WeatherReaderStep1();}

    @Bean
    public WeatherWriteStep2 weatherWriteStep2(){return new WeatherWriteStep2();}

    @Bean
    public WeatherReaderStep3 weatherReaderStep3(){return new WeatherReaderStep3();}

    @Bean
    public WeatherWriteStep4 weatherWriteStep4(){return new WeatherWriteStep4();}

    @Bean
    public WeatherReaderStep4 weatherReaderStep4(){return new WeatherReaderStep4();}



    @Bean
    public Job readCSVTemperature(Step step1,Step step2){
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2)
                .next(step3())
                .next(step4())
                .build();

    }
    @Bean
    public Step step1(WeatherItemWriter writer
            , WeatherItemProcessor weatherItemProcessor
            ,WeatherItemWriterListener weatherItemWriterListener
            ,WeatherItemProcessorListener weatherItemProcessorListener
            ,WeatherItemReaderListener weatherItemReaderListener
            ,WeatherReaderStep1 weatherItemReader) {
        return stepBuilderFactory.get("step1")
                .<Weather, Weather>chunk(100)
                .listener(weatherItemReaderListener)
                .listener(weatherItemProcessorListener)
                .listener(weatherItemWriterListener)
                .faultTolerant()
                .skipLimit(100)
                .skip(IllegalArgumentException.class)
                .reader(weatherItemReader.multiResourceItemReader())
                .processor(weatherItemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("step2")
                .<Weather, WeatherDto>chunk(100)
                .reader(weatherReaderStep1().multiResourceItemReader())
                .processor(new weaterItemProcessorStep2())
                .writer(weatherWriteStep2().weatherFlatFileItemWriter())
                .build();
    }

    @Bean
    public Step step3(){
        return stepBuilderFactory.get("step3")
                .<WeatherRiskDto, WeatherRisk>chunk(100)
                .reader(weatherReaderStep3().jdbcCursorItemReader())
                .processor(new WeatherItemProcessorStep3())
                .writer(weatherRiskItemWriteStep3())
                .build();
    }
    @Bean
    public Step step4(){
        return stepBuilderFactory.get("step4")
                .<WeatherRisk, WeatherRisk>chunk(100)
                .reader(weatherReaderStep4().jdbcCursorItemReaderCount())
                .processor(new WeaterItemProcessorStep4())
                .writer(weatherWriteStep4().weatherRiskFlatFileItemWriter())
                .build();
    }
}
