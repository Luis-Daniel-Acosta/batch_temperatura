package com.bosonit.batch_temperatura.domain.weather;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * A DTO for the {@link Weather} entity
 */
@NoArgsConstructor
@Data
public class WeatherDto implements Serializable {


    private String location;
    private String date;
    private Integer temperature;

    public WeatherDto(Weather weather) {
        this.location = weather.getLocation();
        this.date = new SimpleDateFormat("dd/MM/yyyy").format(weather.getDate());
        this.temperature = weather.getTemperature();
    }
}