package com.bosonit.batch_temperatura.job;

import com.bosonit.batch_temperatura.domain.weather.Weather;
import com.bosonit.batch_temperatura.repository.RepositoryWeather;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

public class WeatherItemReader implements ItemReader<Weather> {

    @Autowired
    RepositoryWeather repositoryWeather;

    private Iterator<Weather> userIterator;

    @BeforeStep
    public void before(StepExecution stepExecution) {
        userIterator = repositoryWeather.findAll().iterator();
    }

    @Override
    public Weather read(){
        if(userIterator != null && userIterator.hasNext()){
        return userIterator.next();
        }else{
        return null;
        }
    }
}
