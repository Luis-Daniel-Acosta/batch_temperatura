package com.bosonit.batch_temperatura.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@Entity
public class WeatherRisk {

    public static final int HIGH = 36;
    public static final int LOW = -24;
    public static final int NORMAL = 32;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String location;
    private Integer month;
    private Integer numberMeasurements;
    private Integer year;
    private double AverageTemperature;
    private int risk;

    @OneToOne
    private Weather weather;

    public WeatherRisk(String location
                        , Integer month
                        , Integer year
                        , int risk
                        , Weather weather) {
        this.location = location;
        this.month = month;
        this.year = year;
        this.risk = risk;
        this.weather = weather;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
