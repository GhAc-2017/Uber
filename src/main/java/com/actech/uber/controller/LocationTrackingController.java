package com.actech.uber.controller;

import com.actech.uber.model.Driver;
import com.actech.uber.model.ExactLocation;
import com.actech.uber.model.Passenger;
import com.actech.uber.repositories.PassengerRepository;
import com.actech.uber.services.Constants;
import com.actech.uber.services.LocationTrackingService;
import com.actech.uber.services.messagequeue.MessageQueue;
import com.actech.uber.utils.UtilityProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
public class LocationTrackingController {
    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    MessageQueue messageQueue;

    @Autowired
    Constants constants;

    @Autowired
    LocationTrackingService locationTrackingService;

    @PutMapping("{driverId}")
    public void updateDriverLocation(@PathVariable Long driverId, @RequestBody ExactLocation data) {
        // once every 3 seconds for each active driver
        Driver driver = UtilityProcessor.getDriverFromId(driverId);
        // todo: check if the driver has an active booking
        //       update the bookings completedRoute based on the driver's location
        //       update the expected completion time
        ExactLocation location = ExactLocation.builder()
                .longitude(data.getLongitude())
                .latitude(data.getLatitude())
                .build();
        messageQueue.sendMessage(constants.getLocationTrackingTopicName(),
                new LocationTrackingService.Message(driver, location));
    }

    @PutMapping("passenger/{passengerId}")
    public void updatePassengerLocation(@PathVariable Long passengerId, @RequestBody ExactLocation location) {
        Passenger passenger = UtilityProcessor.getPassengerFromId(passengerId);
        passenger.setLastKnownLocation(ExactLocation.builder()
        .longitude(location.getLongitude())
        .latitude(location.getLatitude())
        .build());

        passengerRepository.save(passenger);
    }
}
