package com.bosonit.batch_temperatura.job.step2;

import com.bosonit.batch_temperatura.domain.weater.Weather;
import com.bosonit.batch_temperatura.domain.weater.WeatherDto;
import org.springframework.batch.item.ItemProcessor;

public class weaterItemProcessorStep2 implements ItemProcessor<Weather, WeatherDto> {
    @Override
    public WeatherDto process(Weather weather) throws Exception {
        if (weather.getTemperature()>50 || weather.getTemperature()<-20){
            return new WeatherDto(weather);
        }else
            return null;
    }
}
