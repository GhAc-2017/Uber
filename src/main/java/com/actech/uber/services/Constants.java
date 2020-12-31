package com.actech.uber.services;

import com.actech.uber.repositories.DBConstantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Constants {
    final DBConstantRepository dbConstantRepository;

//    private static final Integer LOAD_DELAY_TIME = 60*10*1000;
    private static final Integer TEN_MINUTES = 60 * 10 * 1000;
    private final Map<String, String> constants = new HashMap<>();

    public Constants(DBConstantRepository dbConstantRepository) {
        this.dbConstantRepository = dbConstantRepository;
        loadConstantsFromDB();
    }

    @Scheduled(fixedRate = 60*10)
    private void loadConstantsFromDB() {
        dbConstantRepository.findAll().forEach(dbconstant -> {
            constants.put(dbconstant.getName(), dbconstant.getValue());
        });
    }

    public Integer getRideStartOTPExpiryMinutes(){
        return Integer.parseInt(constants.getOrDefault("rideStartOTPExpiryMinutes", "3610000"));
    }

    public String getSchedulingTopicName() {
        return constants.getOrDefault("schedulingTopicName", "schedulingServiceTopic");
    }

    public String getDriverMatchingTopicName() {
        return constants.getOrDefault("driverMatchingTopicName", "DriverMatchingTopic");
    }

    public int getMaxWaitTimeForPreviousRide() {
        return Integer.parseInt(constants.getOrDefault("maxWaitTimeForPreviousRide", "900000"));
    }

    public Integer getBookingProcessBeforeTime() {
        return Integer.parseInt(constants.getOrDefault("bookingProcessBeforeTime", "900000"));
    }
}