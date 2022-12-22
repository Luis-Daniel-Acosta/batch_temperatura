package com.bosonit.batch_temperatura.job.step4;

import com.bosonit.batch_temperatura.domain.weatherRisk.WeatherRisk;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

public class WeatherReaderStep4 {

    @Autowired
    DataSource dataSource;

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
