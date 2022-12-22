package com.bosonit.batch_temperatura.job.step3;

import com.bosonit.batch_temperatura.domain.weaterRisk.WeatherRiskDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class RowMapperStep3 implements RowMapper<WeatherRiskDto> {

    @Override
    public WeatherRiskDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        WeatherRiskDto weatherRisk= new WeatherRiskDto();
        weatherRisk.setLocation(rs.getString("location"));
        weatherRisk.setYear(rs.getInt("year"));
        weatherRisk.setMonth(rs.getInt("month"));
        weatherRisk.setNumberMeasurements(rs.getInt("numberMeasurements"));
        weatherRisk.setAverageTemperature(rs.getDouble("average"));

        return weatherRisk;
    }
}
