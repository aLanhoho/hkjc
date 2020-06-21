package com.win.big.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class RaceHistIdClass implements Serializable {

    @Column(name="race_date", nullable = false)
    private String race_date;

    @Column(name="race_no", nullable = false)
    private Integer race_no;

    public RaceHistIdClass() {

    }

    public RaceHistIdClass(String raceDate, Integer raceNo) {
        this.race_date = raceDate;
        this.race_no = raceNo;
    }

}
