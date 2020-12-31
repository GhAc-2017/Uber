package com.actech.uber.services;

import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;
import com.actech.uber.services.messagequeue.MQMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class DriverMatchingService {
    public void acceptBooking(Booking booking, Driver driver) {

    }

    public void cancelByDriver(Booking booking, Driver driver) {

    }

    public void assignDriver(Booking booking) {

    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message implements MQMessage {
        private  Booking booking;
    }
}
