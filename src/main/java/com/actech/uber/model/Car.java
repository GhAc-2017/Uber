package com.actech.uber.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "car")
public class Car extends Auditable{
    @ManyToOne
    private Color color;

    private String licensePlateNumber;
    private String brandAndModel;

    private CarType carType;

    @OneToOne
    private Driver driver;
}
