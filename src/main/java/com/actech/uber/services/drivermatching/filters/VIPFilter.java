package com.actech.uber.services.drivermatching.filters;

import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;
import com.actech.uber.services.Constants;

import java.util.List;

public class VIPFilter extends DriverFilter {
    public VIPFilter(Constants constants) {
        super(constants);
    }

    @Override
    public List<Driver> apply(List<Driver> drivers, Booking booking) {
        if (!getConstants().getIsVIPBasedFilterEnabled()) return drivers;
        // if the booking is for a prime or Sedan, then only match drivers > 4 rating
        // todo
        // for each driver, find the avg rating
        return drivers;
    }
}
