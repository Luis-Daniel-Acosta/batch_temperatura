package com.bosonit.batch_temperatura.job.step3;

import com.bosonit.batch_temperatura.domain.weatherRisk.WeatherRisk;
import com.bosonit.batch_temperatura.repository.RespositoryWeatherRisk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WeatherRiskItemWriteStep3 implements ItemWriter<WeatherRisk> {

    @Autowired
    RespositoryWeatherRisk repositoryWeatherRisk;

    @Override
    public void write(List<? extends WeatherRisk> list) throws Exception {

        repositoryWeatherRisk.saveAll(list);

    }
}
