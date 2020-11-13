package com.actech.uber.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "paymentreceipt")
public class PaymentReceipt extends Auditable{
    private Double amount;

    @ManyToOne
    private PaymentGateWay paymentGateWay;

    private String details;
}
