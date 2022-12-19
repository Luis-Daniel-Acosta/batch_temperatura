package com.bosonit.batch_temperatura.listener;

import com.bosonit.batch_temperatura.domain.WeatherRisk;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

@Log4j2
public class WeatherItemWriterListener implements ItemWriteListener<WeatherRisk> {


    @Override
    public void beforeWrite(List<? extends WeatherRisk> list) {
        log.info("beforeWrite");
    }

    @Override
    public void afterWrite(List<? extends WeatherRisk> list) {
        for (WeatherRisk weatherRisk : list){
            log.info("afterWrite :" +weatherRisk.toString());
        }
    }

    @Override
    public void onWriteError(Exception e, List<? extends WeatherRisk> list) {
        log.info("onWriteError");
    }
}
