package com.actech.uber.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "exactlocation")
public class ExactLocation extends Auditable{
    private String  latitude;
    private String  longitude;
}
