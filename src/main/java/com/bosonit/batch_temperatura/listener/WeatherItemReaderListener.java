package com.bosonit.batch_temperatura.listener;

import com.bosonit.batch_temperatura.domain.weater.Weather;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ItemReadListener;

@Log4j2
public class WeatherItemReaderListener implements ItemReadListener<Weather> {


    @Override
    public void beforeRead(){
        log.info("beforeRead");

    }

    @Override
    public void afterRead(Weather weather) {
        log.info("afterRead: "+weather.toString());

    }

    @Override
    public void onReadError(Exception e) {
        log.info("onReadError");

    }
}
