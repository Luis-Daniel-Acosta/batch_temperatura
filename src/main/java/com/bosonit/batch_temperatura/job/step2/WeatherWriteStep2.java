package com.bosonit.batch_temperatura.job.step2;

import com.bosonit.batch_temperatura.domain.weather.WeatherDto;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class WeatherWriteStep2 {

    private Resource outputResource = new FileSystemResource("output/outputDataError.csv");


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

}
