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
import com.bosonit.batch_temperatura.job.step4.RowMapperStep4;
import com.bosonit.batch_temperatura.job.step4.WeaterItemProcessorStep4;
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
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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



//    @Value("classpath*:/input/data.csv")
//    private Resource[] inputResources;

//    private Resource outputResource = new FileSystemResource("output/outputDataError.csv");

    private Resource getOutputResource= new FileSystemResource("output/dataTemperature.csv");


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
                .reader(jdbcCursorItemReaderCount())
                .processor(new WeaterItemProcessorStep4())
                .writer(weatherRiskFlatFileItemWriter())
                .build();
    }

//    @Bean
//    public MultiResourceItemReader<Weather> multiResourceItemReader()
//    {    MultiResourceItemReader<Weather> resourceItemReader = new MultiResourceItemReader<Weather>();
//        resourceItemReader.setResources(inputResources);
//        resourceItemReader.setDelegate(readerr());
//        return resourceItemReader;
//    }

//    Configuracion de Reader para el paso 1
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    @Bean
//    public FlatFileItemReader<Weather> readerr()
//    {
//        //Create reader instance
//        FlatFileItemReader<Weather> reader = new FlatFileItemReader<Weather>();
//        //Set number of lines to skips. Use it if file has header rows.
//        reader.setLinesToSkip(1);
//        //Configure how each line will be parsed and mapped to different values
//        reader.setLineMapper(new DefaultLineMapper() {
//            {
//                //3 columns in each row
//                setLineTokenizer(new DelimitedLineTokenizer() {
//                    {
//                        setNames(new String[] { "Location", "Date", "Temperature" });
//                    }
//                });
//                //Set values in Employee class
//                setFieldSetMapper(new BeanWrapperFieldSetMapper<Weather>() {
//                    {
//                        setTargetType(Weather.class);
//                    }
//                });
//            }
//        });
//        return reader;
//    }


// confiuracion Writer para el paso 2
//    public FlatFileItemWriter<WeatherDto> weatherFlatFileItemWriter()
//    {
//        //Create reader instance
//        FlatFileItemWriter<WeatherDto> writeDto = new FlatFileItemWriter<>();
//        writeDto.setResource(outputResource);
//        //All job repetitions should "append" to same output file
//        writeDto.setAppendAllowed(true);
//        //Configure how each line will be parsed and mapped to different values
//        writeDto.setLineAggregator(new DelimitedLineAggregator<WeatherDto>() {
//            {
//                setDelimiter(",");
//                setFieldExtractor(new BeanWrapperFieldExtractor<WeatherDto>() {
//                    {
//                        setNames(new String[] { "location", "date", "temperature" });
//                    }
//                });
//            }
//        });
//        return writeDto;
//    }

//    confiuracion Reader para el paso 3
//    @Bean
//    public JdbcCursorItemReader<WeatherRiskDto> jdbcCursorItemReader(){
//        JdbcCursorItemReader<WeatherRiskDto> reader3= new JdbcCursorItemReader<>();
//        reader3.setSql("SELECT weatherRiskDto.location, YEAR(weatherRiskDto.date) as year,MONTH(weatherRiskDto.date) as month, COUNT(weatherRiskDto.temperature) as numberMeasurements, AVG(weatherRiskDto.temperature) as average" +
//                        " FROM weather AS weatherRiskDto "+
//                        "GROUP BY weatherRiskDto.location");
//        reader3.setDataSource(dataSource);
//        reader3.setFetchSize(100);
//        reader3.setRowMapper(new RowMapperStep3());
//        return reader3;
//    }

//    confiuracion Writer para el paso 4
    public FlatFileItemWriter<WeatherRisk> weatherRiskFlatFileItemWriter()
    {
        //Create reader instance
        FlatFileItemWriter<WeatherRisk> writeDto = new FlatFileItemWriter<>();
        writeDto.setResource(getOutputResource);
        //All job repetitions should "append" to same output file
        writeDto.setAppendAllowed(true);
        //Configure how each line will be parsed and mapped to different values
        writeDto.setLineAggregator(new DelimitedLineAggregator<WeatherRisk>() {
            {
                setDelimiter(",");
                setFieldExtractor(new BeanWrapperFieldExtractor<WeatherRisk>() {
                    {
                        setNames(new String[] { "location", "month", "year","numberMeasurements","averageTemperature","risk" });
                    }
                });
            }
        });
        return writeDto;
    }

//    confiuracion Reader para el step 4
    @Bean
    public JdbcCursorItemReader<WeatherRisk> jdbcCursorItemReaderCount(){
        JdbcCursorItemReader<WeatherRisk> reader4= new JdbcCursorItemReader<>();
        reader4.setSql("SELECT t.location,t.year,t.month,t.numberMeasurements,t.averageTemperature,t.risk"
                + " FROM weather_risk AS t");
        reader4.setDataSource(dataSource);
        reader4.setFetchSize(100);
        reader4.setRowMapper(new RowMapperStep4());
        return reader4;
    }


}
