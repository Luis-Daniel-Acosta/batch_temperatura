package com.bosonit.batch_temperatura.repository;

import com.bosonit.batch_temperatura.domain.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryWeather extends JpaRepository<Weather,Long> {
}
