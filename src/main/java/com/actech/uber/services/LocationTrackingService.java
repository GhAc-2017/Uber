package com.actech.uber.services;

import com.actech.uber.model.Driver;
import com.actech.uber.model.ExactLocation;
import com.actech.uber.repositories.DriverRepository;
import com.actech.uber.services.messagequeue.MQMessage;
import com.actech.uber.services.messagequeue.MessageQueue;
import com.actech.uber.utils.quadtree.QuadTree;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocationTrackingService {
    final MessageQueue messageQueue;
    final Constants constants;
    final DriverRepository driverRepository;

    QuadTree world = new QuadTree();

    public LocationTrackingService(MessageQueue messageQueue, Constants constants, DriverRepository driverRepository) {
        this.messageQueue = messageQueue;
        this.constants = constants;
        this.driverRepository = driverRepository;
    }

    @Scheduled(fixedRate = 1000)
    public void consumer() {
        MQMessage mqMessage = messageQueue.consumeMessage(constants.getDriverMatchingTopicName());
        if(mqMessage == null)
            return;
        Message message = (Message) mqMessage;
        updateDriverLocation(message.getDriver(), message.getLocation());
    }

    public List<Driver> getDriversNearLocation(ExactLocation pickup) {
        return world.findNeighboursIds(pickup.getLatitude(), pickup.getLongitude(), constants.getMaxDistanceKmForDriverMatching())
        .stream()
        .map(driverId -> driverRepository.findById(driverId).orElseThrow())
        .collect(Collectors.toList());
    }

    public void updateDriverLocation(Driver driver, ExactLocation location) {
        //add neighbour to corresponding location as the driver changes location
        world.addNeighbour(driver.getId(), location.getLatitude(), location.getLongitude());
        driver.setLastKnownLocation(location);
        driverRepository.save(driver);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message implements MQMessage {
        private Driver driver;
        private ExactLocation location;
    }
}
