package com.actech.uber.services;

import com.actech.uber.model.Booking;
import org.springframework.stereotype.Service;

@Service
public class DefaultSchedulingService implements SchedulingService {
    @Override
    public void schedule(Booking booking) {
        // if its time to activate the booking
    }

    public static void main(String[] args) {

    }
}
