package com.bosonit.batch_temperatura.domain.weaterRisk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * A DTO for the {@link WeatherRisk} entity
 */
//@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherRiskDto implements Serializable {

    private String location;
    private Integer month;
    private Integer numberMeasurements;
    private Integer year;
    private double averageTemperature;

    public WeatherRiskDto(String location, Integer month, Integer numberMeasurements, Integer year, double averageTemperature) {

        this.location = location;
        this.month = month;
        this.numberMeasurements = numberMeasurements;
        this.year = year;
        this.averageTemperature = averageTemperature;
    }
}