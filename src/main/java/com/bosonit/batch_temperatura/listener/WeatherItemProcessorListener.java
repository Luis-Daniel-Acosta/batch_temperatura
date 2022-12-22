package com.bosonit.batch_temperatura.listener;

import com.bosonit.batch_temperatura.domain.weather.Weather;
import com.bosonit.batch_temperatura.domain.weatherRisk.WeatherRisk;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ItemProcessListener;

@Log4j2
public class WeatherItemProcessorListener implements ItemProcessListener<Weather, WeatherRisk> {

    @Override
    public void beforeProcess(Weather weather) {
        log.info("beforeProcess");
    }

    @Override
    public void afterProcess(Weather weather, WeatherRisk weatherRisk) {
        log.info("afterProcess: "+weather+" ---> "+ weatherRisk);
    }

    @Override
    public void onProcessError(Weather weather, Exception e) {
        log.info("onProcessError");

    }
}
