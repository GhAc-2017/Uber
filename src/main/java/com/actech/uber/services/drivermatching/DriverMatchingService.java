package com.actech.uber.services.drivermatching;

import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;
import com.actech.uber.model.ExactLocation;
import com.actech.uber.repositories.BookingRepository;
import com.actech.uber.services.Constants;
import com.actech.uber.services.ETAService;
import com.actech.uber.services.LocationTrackingService;
import com.actech.uber.services.messagequeue.MQMessage;
import com.actech.uber.services.messagequeue.MessageQueue;
import com.actech.uber.services.notifications.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverMatchingService {
    final MessageQueue messageQueue;
    final Constants constants;
    final LocationTrackingService locationTrackingService;
    final NotificationService notificationService;
    final BookingRepository bookingRepository;
    final ETAService etaService;

    @Scheduled(fixedRate = 1000)
    public void consumer() {
        MQMessage mqMessage = messageQueue.consumeMessage(constants.getDriverMatchingTopicName());
        if(mqMessage == null)
            return;
        Message message = (Message) mqMessage;
        findNearbyDrivers(message.booking);
    }
    private void findNearbyDrivers(Booking booking){
        ExactLocation pickup = booking.getPickupLocation();
        List<Driver> drivers = locationTrackingService.getDriversNearLocation(pickup);
        if(drivers.size()==0){
            // todo: add surge fee and send notifications to nearby drivers about the surge
            notificationService.notify(booking.getPassenger().getPhoneNumber(), "No cabs near you !");
            return;
        }

        notificationService.notify(booking.getPassenger().getPhoneNumber(), String.format("Contacting %s cabs around you", drivers.size()));
        // todo: Chain of Responsibility pattern
        // filter the drivers somehow

        if(drivers.size()==0){
            notificationService.notify(booking.getPassenger().getPhoneNumber(), "No cabs near you");
        }

        drivers.forEach(driver -> {
            notificationService.notify(booking.getPassenger().getPhoneNumber(), "Booking near you: "+ booking.toString());
            driver.getAcceptableBookings().add(booking);
        });
        bookingRepository.save(booking);
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Message implements MQMessage {
        private  Booking booking;

        @Override
        public String toString() {
            return String.format("Need to find drivers for %s", booking.toString();
        }
    }
}
