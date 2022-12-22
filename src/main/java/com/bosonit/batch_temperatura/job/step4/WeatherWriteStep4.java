package com.bosonit.batch_temperatura.job.step4;

import com.bosonit.batch_temperatura.domain.weatherRisk.WeatherRisk;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class WeatherWriteStep4 {

    private Resource getOutputResource= new FileSystemResource("output/dataTemperature.csv");

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
}
