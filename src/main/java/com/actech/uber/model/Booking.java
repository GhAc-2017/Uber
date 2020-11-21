package com.actech.uber.model;

import com.actech.uber.exception.InvalidActionForBookingStateException;
import com.actech.uber.exception.InvalidOTPException;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.actech.uber.model.constants.RIDE_START_OTP_EXPIRY_MINUTES;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "booking", indexes = {
        @Index(columnList = "passenger_id"),
        @Index(columnList = "driver_id")
})
public class Booking extends Auditable{
    @ManyToOne
    private Passenger passenger;

    @ManyToOne
    private Driver driver;

    private BookingType bookingType;

    @OneToOne
    private Review reviewByPassenger;

    @OneToOne
    private Review reviewByDriver;

    @OneToOne
    private PaymentReceipt paymentReceipt;

    private BookingStatus bookingStatus;

    @OneToMany
    @JoinTable(
            name = "booking_route",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "exact_location_id"),
            indexes = {@Index(columnList = "booking_id")}
    )
    @OrderColumn(name = "location_index")
    private List<ExactLocation> route;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime;

    private Long totalDistanceinMeters;

    @OneToOne
    private OTP rideStartOtp;

    public void startRide(OTP otp){
        if(!bookingStatus.equals(BookingStatus.CAB_ARRIVED))
            throw new InvalidActionForBookingStateException("Cannot start the ride before the driver has reached the location");

        if(!rideStartOtp.validateOTP(otp, RIDE_START_OTP_EXPIRY_MINUTES))
            throw new InvalidOTPException();

        bookingStatus = BookingStatus.IN_RIDE;
    }

    public void endRide() {
        if(!bookingStatus.equals(BookingStatus.IN_RIDE))
            throw new InvalidActionForBookingStateException("The ride hasent started yet");
        bookingStatus = BookingStatus.COMPLETED;
    }
}
