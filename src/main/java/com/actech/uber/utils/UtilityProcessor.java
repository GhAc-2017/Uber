package com.actech.uber.utils;

import com.actech.uber.exception.InValidDriverException;
import com.actech.uber.exception.InValidPassengerException;
import com.actech.uber.model.Driver;
import com.actech.uber.model.Passenger;
import com.actech.uber.repositories.DriverRepository;
import com.actech.uber.repositories.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UtilityProcessor {
    @Autowired
    static DriverRepository driverRepository;

    @Autowired
    static PassengerRepository passengerRepository;

    public static Driver getDriverFromId(Long driverId){
        Optional<Driver> driver = driverRepository.findById(driverId);
        if(driver.isEmpty()) {
            throw new InValidDriverException("No Driver with id "+ driverId);
        }
        return driver.get();
    }

    public static Passenger getPassengerFromId(Long passengerId){
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if(passenger.isEmpty()) {
            throw new InValidPassengerException("No Passenger with id "+ passengerId);
        }
        return passenger.get();
    }
}
