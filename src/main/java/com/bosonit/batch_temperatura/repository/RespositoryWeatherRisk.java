package com.bosonit.batch_temperatura.repository;

import com.bosonit.batch_temperatura.domain.WeatherRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespositoryWeatherRisk extends JpaRepository<WeatherRisk, Long> {
}
