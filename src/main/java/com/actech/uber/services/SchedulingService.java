package com.actech.uber.services;

import com.actech.uber.model.Booking;
import com.actech.uber.services.messagequeue.MQMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class SchedulingService {
    BookingService bookingService;

    public void schedule(Booking booking) {
        // if its time to activate the booking
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message implements MQMessage {
        private  Booking booking;
    }
}
