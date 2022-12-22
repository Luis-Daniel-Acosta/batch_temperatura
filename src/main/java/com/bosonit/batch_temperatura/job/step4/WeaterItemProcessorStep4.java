package com.bosonit.batch_temperatura.job.step4;

import com.bosonit.batch_temperatura.domain.weaterRisk.WeatherRisk;
import com.bosonit.batch_temperatura.domain.weaterRisk.WeatherRiskDto;
import org.springframework.batch.item.ItemProcessor;

public class WeaterItemProcessorStep4 implements ItemProcessor<WeatherRisk, WeatherRisk> {


    @Override
    public WeatherRisk process(WeatherRisk weatherRisk) throws Exception {
        return weatherRisk;
    }
}
