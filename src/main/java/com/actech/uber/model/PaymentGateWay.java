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
@Table(name = "paymentgatway")
public class PaymentGateWay extends Auditable{
    private String name;
}
