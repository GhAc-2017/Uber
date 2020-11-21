package com.actech.uber.services;

import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;
import org.springframework.stereotype.Service;

@Service
public class SimpleDriverMatchingService implements DriverMatchingService {
    @Override
    public void acceptBooking(Booking booking, Driver driver) {

    }

    @Override
    public void cancelByDriver(Booking booking, Driver driver) {

    }

    @Override
    public void assignDriver(Booking booking) {

    }

    public static void main(String[] args) {
        //consumer
        //for each request
        // call the appropriate methods
    }
}
