package com.actech.uber.services;

import com.actech.uber.model.Booking;
import com.actech.uber.model.BookingStatus;
import com.actech.uber.model.Driver;
import com.actech.uber.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class DefaultBookingService implements BookingService {
    @Autowired
    DriverMatchingService driverMatchingService;

    @Autowired
    SchedulingService schedulingService;

    @Autowired
    OTPService otpService;

    @Override
    public void createBooking(Booking booking, Passenger passenger) {
        if(booking.getStartTime().after(new Date())){
            booking.setBookingStatus(BookingStatus.SCHEDULED);
            //use a task queue to push this task to assign a driver
            //producer
            schedulingService.schedule(booking);
        }else{
            booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
            otpService.sendRideStartOTP(booking.getRideStartOtp());
            {
                //use a task queue to push this task to assign a driver
                //producer
                driverMatchingService.assignDriver(booking);
            }
        }
    }

    @Override
    public void cancelByPassenger(Booking booking, Passenger passenger) {

    }

    @Override
    public void acceptBooking(Booking booking, Driver driver) {

    }

    @Override
    public void cancelByDriver(Driver driver, Booking booking) {

    }
}
