package com.actech.uber.repositories;

import com.actech.uber.model.PaymentGateWay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentGatewayRepository extends JpaRepository<PaymentGateWay, Long> {
}
