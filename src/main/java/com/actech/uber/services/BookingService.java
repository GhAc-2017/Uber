package com.actech.uber.services;

import com.actech.uber.exception.InvalidActionForBookingStateException;
import com.actech.uber.model.*;
import com.actech.uber.repositories.BookingRepository;
import com.actech.uber.repositories.DriverRepository;
import com.actech.uber.repositories.PassengerRepository;
import com.actech.uber.services.drivermatching.DriverMatchingService;
import com.actech.uber.services.messagequeue.MessageQueue;
import com.actech.uber.services.notifications.NotificationService;
import com.actech.uber.services.otp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {
    @Autowired
    DriverMatchingService driverMatchingService;

    @Autowired
    SchedulingService schedulingService;

    @Autowired
    OTPService otpService;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    MessageQueue messageQueue;

    @Autowired
    Constants constants;

    @Autowired
    NotificationService notificationService;

//    public void createBooking(Booking booking, Passenger passenger) {
//        if(booking.getStartTime().after(new Date())){
//            booking.setBookingStatus(BookingStatus.SCHEDULED);
//            //use a task queue to push this task to assign a driver
//            //producer
//            messageQueue.sendMessage(constants.getSchedulingTopicName(), new SchedulingService.Message(booking));
//            schedulingService.schedule(booking);
//        }else{
//            booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
//            otpService.sendRideStartOTP(booking.getRideStartOtp());
//            messageQueue.sendMessage(constants.getDriverMatchingTopicName(), new DriverMatchingService.Message(booking));
//            {
//                //use a task queue to push this task to assign a driver
//                //producer
//                driverMatchingService.assignDriver(booking);
//            }
//        }
//        bookingRepository.save(booking);
//        passengerRepository.save(booking.getPassenger());
//    }

    public void createBooking(Booking booking) {
        if (booking.getStartTime().after(new Date())) {
            booking.setBookingStatus(BookingStatus.SCHEDULED);
            messageQueue.sendMessage(constants.getSchedulingTopicName(), new SchedulingService.Message(booking));
        } else {
            booking.setBookingStatus(BookingStatus.ASSIGNING_DRIVER);
            otpService.sendRideStartOTP(booking.getRideStartOTP());
            messageQueue.sendMessage(constants.getDriverMatchingTopicName(), new DriverMatchingService.Message(booking));
        }
        bookingRepository.save(booking);
        passengerRepository.save(booking.getPassenger());
    }

    public void acceptBooking(Driver driver, Booking booking) {
        if (!booking.needsDriver()) {
            return;
        }
        if (!driver.canAcceptBooking(constants.getMaxWaitTimeForPreviousRide())) {
            notificationService.notify(driver.getPhoneNumber(), "Cannot accept booking");
            return;
        }
        booking.setDriver(driver);
        driver.setActiveBooking(booking);
        booking.getNotifiedDrivers().clear();
        driver.getAcceptableBookings().clear();
        notificationService.notify(booking.getPassenger().getPhoneNumber(), driver.getName() + " is arriving at pickup location");
        notificationService.notify(driver.getPhoneNumber(), "Booking accepted");
        bookingRepository.save(booking);
        driverRepository.save(driver);
    }

    public void cancelByDriver(Driver driver, Booking booking) {
        booking.setDriver(null);
        driver.setActiveBooking(null);
        driver.getAcceptableBookings().remove(booking);
        notificationService.notify(booking.getPassenger().getPhoneNumber(),
                "Reassigning driver");
        notificationService.notify(driver.getPhoneNumber(), "Booking has been cancelled");
        retryBooking(booking);
    }

    public void cancelByPassenger(Passenger passenger, Booking booking) {
        try {
            booking.cancel();
            bookingRepository.save(booking);
        } catch (InvalidActionForBookingStateException inner) {
            notificationService.notify(booking.getPassenger().getPhoneNumber(),
                    "Cannot cancel the booking now. If the ride is in progress, ask your driver to end the ride"
            );
            throw inner;
        }
    }

    public void updateRoute(Booking booking, List<ExactLocation> route) {
        if (!booking.canChangeRoute()) {
            throw new InvalidActionForBookingStateException("Ride has already been completed or cancelled");
        }
        booking.setRoute(route);
        bookingRepository.save(booking);
        notificationService.notify(booking.getDriver().getPhoneNumber(), "Route has been updated!");
    }

    public void retryBooking(Booking booking) {
        createBooking(booking);
    }
}
