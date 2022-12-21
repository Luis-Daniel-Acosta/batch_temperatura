package com.bosonit.batch_temperatura.domain.weater;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Weather {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String location;
    private Date date;
    private Integer temperature;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
