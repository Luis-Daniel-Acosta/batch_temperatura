package com.bosonit.batch_temperatura.job.step1;

import com.bosonit.batch_temperatura.domain.weater.Weather;
import org.springframework.batch.item.ItemProcessor;

public class WeatherItemProcessor implements ItemProcessor<Weather, Weather> {


    @Override
    public Weather process(Weather weather) throws Exception {

        if (weather.getTemperature()>50 || weather.getTemperature()<-20){
            throw new IllegalArgumentException();
        }else
            return weather;
    }


}
