package com.bosonit.batch_temperatura.job.step1;

import com.bosonit.batch_temperatura.domain.weather.Weather;
import com.bosonit.batch_temperatura.repository.RepositoryWeather;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WeatherItemWriter implements ItemWriter<Weather> {

    @Autowired
    RepositoryWeather repositoryWeather;


    @Override
    public void write(List<? extends Weather> list) throws Exception {
        repositoryWeather.saveAll(list);

    }
}
