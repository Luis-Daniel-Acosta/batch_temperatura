package com.bosonit.batch_temperatura.job.step3;

import com.bosonit.batch_temperatura.domain.weatherRisk.WeatherRisk;
import com.bosonit.batch_temperatura.domain.weatherRisk.WeatherRiskDto;
import org.springframework.batch.item.ItemProcessor;

public class WeatherItemProcessorStep3 implements ItemProcessor<WeatherRiskDto, WeatherRisk> {


    @Override
    public WeatherRisk process(WeatherRiskDto weatherRiskDto) throws Exception {
        WeatherRisk risk=new WeatherRisk();

        risk.setYear(weatherRiskDto.getYear());
        risk.setMonth(weatherRiskDto.getMonth());
        risk.setLocation(weatherRiskDto.getLocation());
        risk.setNumberMeasurements(weatherRiskDto.getNumberMeasurements());
        risk.setAverageTemperature(weatherRiskDto.getAverageTemperature());
        if(weatherRiskDto.getAverageTemperature()>=36)
            risk.setRisk("HIGH");
        else if (risk.getAverageTemperature()<36 && risk.getAverageTemperature()>32)
            risk.setRisk("MEDIUM");
        else
            risk.setRisk("LOW");
        return risk;
    }
}
