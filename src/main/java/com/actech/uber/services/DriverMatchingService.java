package com.actech.uber.services;

import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;

public interface DriverMatchingService {
    void acceptBooking(Booking booking, Driver driver);

    void cancelByDriver(Booking booking, Driver driver);

    void assignDriver(Booking booking);
}
