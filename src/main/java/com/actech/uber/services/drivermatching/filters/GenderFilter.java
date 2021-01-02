package com.actech.uber.services.drivermatching.filters;

import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;
import com.actech.uber.model.Gender;
import com.actech.uber.services.Constants;

import java.util.List;
import java.util.stream.Collectors;

public class GenderFilter extends DriverFilter{
    public GenderFilter(Constants constants) {
        super(constants);
    }

    @Override
    public List<Driver> apply(List<Driver> drivers, Booking booking) {
        if(!getConstants().getIsGenderFilterEnabled()) return drivers;
        Gender passengerGender = booking.getPassenger().getGender();
        return drivers.stream().filter(driver -> {
            Gender driverGender = driver.getGender();
            return !driverGender.equals(Gender.MALE) || passengerGender.equals(Gender.MALE);
        }).collect(Collectors.toList());
    }
}
