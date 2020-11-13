package com.actech.uber.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "namedlocation")
public class NamedLocation extends Auditable{
    @OneToOne
    private ExactLocation exactLocation;

    private String name;
    private String zipCode;
    private String city;
    private String country;
    private String state;
}
