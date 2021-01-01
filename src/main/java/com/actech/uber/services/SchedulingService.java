package com.actech.uber.services;

import com.actech.uber.model.Booking;
import com.actech.uber.model.DateUtils;
import com.actech.uber.repositories.BookingRepository;
import com.actech.uber.services.messagequeue.MQMessage;
import com.actech.uber.services.messagequeue.MessageQueue;
import com.actech.uber.services.notifications.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class SchedulingService {
    @Autowired
    MessageQueue messageQueue;
    @Autowired
    Constants constants;
    @Autowired
            LocationTrackingService locationTrackingService;
    @Autowired
    NotificationService notificationService;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BookingService bookingService;

    Set<Booking> scheduledBookings = new HashSet<>();

    @Scheduled(fixedRate = 1000)
    public void consumer() {
        MQMessage mqMessage = messageQueue.consumeMessage(constants.getSchedulingTopicName());
        if(mqMessage == null)
            return;
        Message message = (Message) mqMessage;
        schedule(message.getBooking());
    }

    public void schedule(Booking booking) {
        // if its time to activate the booking
        scheduledBookings.add(booking);
    }

    @Scheduled(fixedRate = 60000)
    public void process() {
        Set<Booking> newSchedulesBooking = new HashSet<>();
        for(Booking booking: scheduledBookings) {
            if(DateUtils.addMinutes(new Date(), constants.getBookingProcessBeforeTime()).after(booking.getScheduledTime())) {
                bookingService.acceptBooking(booking.getDriver(), booking);
            } else {
                newSchedulesBooking.add(booking);
            }
        }
        scheduledBookings = newSchedulesBooking;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message implements MQMessage {
        private  Booking booking;
    }
}
