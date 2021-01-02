package com.actech.uber.services.drivermatching.filters;

import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;
import com.actech.uber.services.Constants;
import lombok.Getter;

import java.util.List;

public abstract class DriverFilter {
    @Getter
    private Constants constants;

    public DriverFilter(Constants constants) {
        this.constants = constants;
    }

    public abstract List<Driver> apply(List<Driver> drivers, Booking booking);
}
