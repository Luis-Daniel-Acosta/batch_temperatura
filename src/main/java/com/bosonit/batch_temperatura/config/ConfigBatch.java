package com.bosonit.batch_temperatura.config;

import com.bosonit.batch_temperatura.domain.weater.Weather;
import com.bosonit.batch_temperatura.domain.weater.WeatherDto;
import com.bosonit.batch_temperatura.domain.weaterRisk.WeatherRisk;
import com.bosonit.batch_temperatura.job.step1.WeatherItemProcessor;
import com.bosonit.batch_temperatura.job.WeatherItemReader;
import com.bosonit.batch_temperatura.job.step1.WeatherItemWriter;
import com.bosonit.batch_temperatura.job.step2.weaterItemProcessorStep2;
import com.bosonit.batch_temperatura.listener.WeatherItemProcessorListener;
import com.bosonit.batch_temperatura.listener.WeatherItemReaderListener;
import com.bosonit.batch_temperatura.listener.WeatherItemWriterListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataAccessException;

@Configuration
@EnableBatchProcessing
public class ConfigBatch {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


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




    @Value("classpath*:/input/data.csv")
    private Resource[] inputResources;

    private Resource outputResource = new FileSystemResource("output/outputDataErrores.csv");


    @Bean
    public Job readCSVTemperature(Step step1){
        return jobBuilderFactory.get("job1")
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .next(step2())
                .build();

    }
    @Bean
    public Step step1(WeatherItemWriter writer
            , WeatherItemProcessor weatherItemProcessor
            ,WeatherItemWriterListener weatherItemWriterListener
            ,WeatherItemProcessorListener weatherItemProcessorListener
            ,WeatherItemReaderListener weatherItemReaderListener
            ,WeatherItemReader weatherItemReader) {
        return stepBuilderFactory.get("step1")
                .<Weather, Weather>chunk(100)
                .listener(weatherItemReaderListener)
                .listener(weatherItemProcessorListener)
                .listener(weatherItemWriterListener)
                .faultTolerant()
                .skipLimit(100)
                .skip(IllegalArgumentException.class)
                .reader(multiResourceItemReader())
                .processor(weatherItemProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step step2(){
        return stepBuilderFactory.get("step2")
                .<Weather, WeatherDto>chunk(10)
                .reader(multiResourceItemReader())
                .processor(new weaterItemProcessorStep2())
                .writer(weatherFlatFileItemWriter())
                .build();
    }

    @Bean
    public Step step3(){
        return stepBuilderFactory.get("step3")
                .<Weather, WeatherDto>chunk(10)
                .reader(multiResourceItemReader())
                .processor(new weaterItemProcessorStep2())
                .writer(weatherFlatFileItemWriter())
                .build();
    }


    @Bean
    public MultiResourceItemReader<Weather> multiResourceItemReader()
    {
        MultiResourceItemReader<Weather> resourceItemReader = new MultiResourceItemReader<Weather>();
        resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(readerr());
        return resourceItemReader;

    }

//                                                                       Configuracion de Reader para el paso 1
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
                        setNames(new String[] { "Location", "Date", "Temperature" });
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


//                                                                         confiuracion WriterDto para pa el paso 2
    public FlatFileItemWriter<WeatherDto> weatherFlatFileItemWriter()
    {
        //Create reader instance
        FlatFileItemWriter<WeatherDto> writeDto = new FlatFileItemWriter<>();

        writeDto.setResource(outputResource);
        //All job repetitions should "append" to same output file
        writeDto.setAppendAllowed(true);

        //Configure how each line will be parsed and mapped to different values
        writeDto.setLineAggregator(new DelimitedLineAggregator<WeatherDto>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<WeatherDto>() {
                    {
                        setNames(new String[] { "location", "date", "temperature" });
                    }
                });
            }
        });
        return writeDto;
    }

    public JpaCursorItemReader<WeatherRisk> weatherRiskJpaCursorItemReader()throws DataAccessException {
//    JpaCursorItemReaderBuilder<WeatherRisk> reader=new JpaCursorItemReader<>();
    return new JpaCursorItemReaderBuilder<WeatherRisk>()
            .queryString("SELECT weatherRiskDto.location, weatherRiskDto.month, weatherRiskDto.number_Measurements" +
                ", weatherRiskDto.year, weatherRiskDto.average_temperature"
                +"FROM weaterRisk AS weatherRiskDto")
            .build();
//        reader.
    }


}
