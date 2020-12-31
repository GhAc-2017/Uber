package com.actech.uber.services;

import com.actech.uber.model.Driver;
import com.actech.uber.model.ExactLocation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationTrackingService {
    public List<Driver> getDriversNearLocation(ExactLocation pickup) {
        return null; // todo
    }
}
