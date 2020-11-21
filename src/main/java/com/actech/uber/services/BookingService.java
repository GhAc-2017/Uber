package com.actech.uber.services;

import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;
import com.actech.uber.model.Passenger;

public interface BookingService {
    public void createBooking(Booking booking, Passenger passenger) ;

    void cancelByDriver(Driver driver, Booking booking);

    void acceptBooking(Booking booking, Driver driver);

    void cancelByPassenger(Booking booking, Passenger passenger);
}
