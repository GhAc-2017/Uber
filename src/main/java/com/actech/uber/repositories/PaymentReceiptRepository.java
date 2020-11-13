package com.actech.uber.repositories;

import com.actech.uber.model.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
}
