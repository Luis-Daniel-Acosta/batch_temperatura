package com.bosonit.batch_temperatura.domain.weaterRisk;

import com.bosonit.batch_temperatura.domain.weater.Weather;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
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
    @Column(name="numbermeasurements")
    private Integer numberMeasurements;
    private Integer year;
    @Column(name="averagetemperature")
    private double averageTemperature;
    private String risk;

    @OneToOne
    @JoinColumn(name="id")
    private Weather weather;

    public WeatherRisk(String location
                        , Integer month
                        , Integer year
                        , String risk
                        ,Integer numberMeasurements
                        ,double averageTemperature
                        , Weather weather) {
        this.location = location;
        this.month = month;
        this.year = year;
        this.risk = risk;
        this.numberMeasurements= numberMeasurements;
        this.averageTemperature=averageTemperature;
        this.weather = weather;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
