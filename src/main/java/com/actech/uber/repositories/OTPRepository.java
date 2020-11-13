package com.actech.uber.repositories;

import com.actech.uber.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OTPRepository extends JpaRepository<OTP, Long> {
}
