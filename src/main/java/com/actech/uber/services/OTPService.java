package com.actech.uber.services;

import com.actech.uber.model.OTP;

public interface OTPService {
    void sendPhoneNumberConfirmationOTP(OTP otp);
    void sendRideStartOTP(OTP otp);
}
