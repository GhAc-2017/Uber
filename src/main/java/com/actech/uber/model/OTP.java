package com.actech.uber.model;

import com.actech.uber.exception.InvalidOTPException;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp")
public class OTP extends Auditable{
    private String code;
    private String sendToNumber;

    private Date expiryTime;

    public boolean validateOTP(OTP otp, Long rideStartOtpExpiryMinutes) {
        if(!code.equals(otp.getCode()))
            return false;
        //check otp expiry then
        return true;

    }
}
