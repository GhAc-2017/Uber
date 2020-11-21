package com.actech.uber.controller;

import com.actech.uber.exception.InValidPassengerException;
import com.actech.uber.exception.InvalidBookingException;
import com.actech.uber.model.*;
import com.actech.uber.repositories.BookingRepository;
import com.actech.uber.repositories.PassengerRepository;
import com.actech.uber.repositories.ReviewRepository;
import com.actech.uber.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/passenger")
@RestController
public class PassengerController {
    @Autowired
    PassengerRepository passengerRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    BookingService bookingService;

    public Passenger getPassengerFromId(Long passengerId){
        Optional<Passenger> passenger = passengerRepository.findById(passengerId);
        if(passenger.isEmpty()) {
            throw new InValidPassengerException("No Passenger with id "+ passengerId);
        }
        return passenger.get();
    }

    public Booking getPassengerBookingFromId(Long bookingId, Passenger passenger){
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if(optionalBooking.isEmpty())
            throw new InvalidBookingException("No booking with id "+ optionalBooking);
        Booking booking = optionalBooking.get();
        if(!booking.getPassenger().equals(passenger))
            throw new InvalidBookingException("Passenger "+passenger.getBookings() + " has no such booking "+bookingId);
        return booking;
    }

    @GetMapping("/{passengerId}")
    public Passenger getPassengerDetails(@RequestParam(name = "passengerId") Long passengerId){
        return getPassengerFromId(passengerId);
    }

    @GetMapping("{passengerId}/bookings")
    public List<Booking> getAllBookings(@RequestParam(name = "passengerId") Long passengerId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return passenger.getBookings();
    }

    @GetMapping("{passengerId}/bookings/{bookingId}")
    public Booking getBooking(@RequestParam(name = "passengerId") Long passengerId,
                              @RequestParam(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return getPassengerBookingFromId(bookingId, passenger);
    }

    @PostMapping("{passengerId}/bookings/")
    public void requestBooking(@RequestParam(name = "passengerId") Long passengerId, @RequestBody Booking data){
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = Booking.builder()
                .build();
        bookingRepository.save(booking);
        passengerRepository.save(passenger);
        bookingService.createBooking(booking, passenger);
    }

    @DeleteMapping("{passengerId}/bookings/{bookingId}")
    public void cancelBooking(@RequestParam(name = "passengerId") Long passengerId, @RequestParam(name = "bookingId") Long bookingId){
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        bookingService.cancelByPassenger(booking, passenger);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}/start")
    public void startRide(@RequestParam(name = "passengerId") Long passengerId, @RequestParam(name = "bookingId") Long bookingId, @RequestBody OTP otp){
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        booking.startRide(otp);
        bookingRepository.save(booking);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}/end")
    public void endRide(@RequestParam(name = "passengerId") Long passengerId, @RequestParam(name = "bookingId") Long bookingId){
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        booking.endRide();
        bookingRepository.save(booking);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}/rate")
    public void rateRide(@RequestParam(name = "passengerId") Long passengerId, @RequestParam(name = "bookingId") Long bookingId, @RequestBody Review data){
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        Review review = Review.builder()
                .note(data.getNote())
                .ratingOutOfFive(data.getRatingOutOfFive())
                .build();
        booking.setReviewByPassenger(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }
}
