package com.bosonit.batch_temperatura.job.step4;

import com.bosonit.batch_temperatura.domain.weaterRisk.WeatherRisk;
import com.bosonit.batch_temperatura.domain.weaterRisk.WeatherRiskDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class RowMapperStep4 implements RowMapper<WeatherRisk> {

    @Override
    public WeatherRisk mapRow(ResultSet rs, int rowNum) throws SQLException {
        WeatherRisk weatherRisk= new WeatherRisk();
        weatherRisk.setLocation(rs.getString("location"));
        weatherRisk.setYear(rs.getInt("year"));
        weatherRisk.setMonth(rs.getInt("month"));
        weatherRisk.setNumberMeasurements(rs.getInt("numberMeasurements"));
        weatherRisk.setAverageTemperature(rs.getDouble("averageTemperature"));

        return weatherRisk;
    }
}
