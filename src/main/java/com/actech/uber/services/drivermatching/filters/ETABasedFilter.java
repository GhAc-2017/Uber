package com.actech.uber.services.drivermatching.filters;

import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;
import com.actech.uber.model.ExactLocation;
import com.actech.uber.services.Constants;
import com.actech.uber.services.ETAService;

import java.util.List;
import java.util.stream.Collectors;

public class ETABasedFilter extends DriverFilter{
    private final ETAService etaService;

    public ETABasedFilter(ETAService etaService, Constants constants) {
        super(constants);
        this.etaService = etaService;
    }

    @Override
    public List<Driver> apply(List<Driver> drivers, Booking booking) {
        if(!getConstants().getIsETABasedFilterEnabled()) return drivers;
        ExactLocation pickup = booking.getPickupLocation();
        return drivers.stream().filter(driver -> {
            return etaService.getETAMinutes(driver.getLastKnownLocation(), pickup)
 <= getConstants().getMaxDriverETAMinutes();        }).collect(Collectors.toList());
    }
}
