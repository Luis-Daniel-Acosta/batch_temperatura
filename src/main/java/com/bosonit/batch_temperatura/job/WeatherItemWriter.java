package com.bosonit.batch_temperatura.job;

import com.bosonit.batch_temperatura.domain.WeatherRisk;
import com.bosonit.batch_temperatura.repository.RespositoryWeatherRisk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WeatherItemWriter implements ItemWriter<WeatherRisk> {

    @Autowired
    RespositoryWeatherRisk respositoryWeatherRisk;


    @Override
    public void write(List<? extends WeatherRisk> list) throws Exception {
        respositoryWeatherRisk.saveAll(list);

    }
}
