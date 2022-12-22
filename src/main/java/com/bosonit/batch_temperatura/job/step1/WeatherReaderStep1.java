package com.bosonit.batch_temperatura.job.step1;

import com.bosonit.batch_temperatura.domain.weather.Weather;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;

public class WeatherReaderStep1
{
    @Value("classpath*:/input/data.csv")
    private Resource[] inputResources;


    @Bean
    public MultiResourceItemReader<Weather> multiResourceItemReader()
    {    MultiResourceItemReader<Weather> resourceItemReader = new MultiResourceItemReader<Weather>();
        resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(readerr());
        return resourceItemReader;
    }
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public FlatFileItemReader<Weather> readerr()
    {
        //Create reader instance
        FlatFileItemReader<Weather> reader = new FlatFileItemReader<Weather>();
        //Set number of lines to skips. Use it if file has header rows.
        reader.setLinesToSkip(1);
        //Configure how each line will be parsed and mapped to different values
        reader.setLineMapper(new DefaultLineMapper() {
            {
                //3 columns in each row
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("Location", "Date", "Temperature");
                    }
                });
                //Set values in Employee class
                setFieldSetMapper(new BeanWrapperFieldSetMapper<Weather>() {
                    {
                        setTargetType(Weather.class);
                    }
                });
            }
        });
        return reader;
    }
}
