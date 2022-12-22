package com.bosonit.batch_temperatura.job.step3;

import com.bosonit.batch_temperatura.domain.weatherRisk.WeatherRiskDto;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class WeatherReaderStep3 {

    @Autowired
    DataSource dataSource;


    @Bean
    public JdbcCursorItemReader<WeatherRiskDto> jdbcCursorItemReader(){
        JdbcCursorItemReader<WeatherRiskDto> reader3= new JdbcCursorItemReader<>();
        reader3.setSql("SELECT weatherRiskDto.location, YEAR(weatherRiskDto.date) as year,MONTH(weatherRiskDto.date) as month, COUNT(weatherRiskDto.temperature) as numberMeasurements, AVG(weatherRiskDto.temperature) as average" +
                " FROM weather AS weatherRiskDto "+
                "GROUP BY weatherRiskDto.location");
        reader3.setDataSource(dataSource);
        reader3.setFetchSize(100);
        reader3.setRowMapper(new RowMapperStep3());
        return reader3;
    }
}
