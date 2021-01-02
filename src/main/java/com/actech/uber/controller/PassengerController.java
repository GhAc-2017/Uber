package com.actech.uber.controller;

import com.actech.uber.exception.InvalidBookingException;
import com.actech.uber.model.*;
import com.actech.uber.repositories.BookingRepository;
import com.actech.uber.repositories.PassengerRepository;
import com.actech.uber.repositories.ReviewRepository;
import com.actech.uber.services.BookingService;
import com.actech.uber.services.Constants;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.actech.uber.utils.UtilityProcessor.getPassengerFromId;

@RequestMapping("/passenger")
@RestController
public class PassengerController {
    final PassengerRepository passengerRepository;
    final BookingRepository bookingRepository;
    final ReviewRepository reviewRepository;
    final BookingService bookingService;
    final Constants constants;

    public PassengerController(PassengerRepository passengerRepository, BookingRepository bookingRepository, ReviewRepository reviewRepository, BookingService bookingService, Constants constants) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.bookingService = bookingService;
        this.constants = constants;
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
    public Passenger getPassengerDetails(@PathVariable(name = "passengerId") Long passengerId){
        return getPassengerFromId(passengerId);
    }

    @GetMapping("{passengerId}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name = "passengerId") Long passengerId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return passenger.getBookings();
    }

    @GetMapping("{passengerId}/bookings/{bookingId}")
    public Booking getBooking(@PathVariable(name = "passengerId") Long passengerId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Passenger passenger = getPassengerFromId(passengerId);
        return getPassengerBookingFromId(bookingId, passenger);
    }

    @PostMapping("{passengerId}/bookings/")
    public void requestBooking(@PathVariable(name = "passengerId") Long passengerId, @RequestBody Booking data){
        Passenger passenger = getPassengerFromId(passengerId);
        List<ExactLocation> route = new ArrayList<>();
        data.getRoute().forEach(location -> route.add(ExactLocation.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build()));
        Booking booking = Booking.builder()
                .rideStartOTP(OTP.make(passenger.getPhone()))
                .route(route)
                .passenger(passenger)
                .bookingType(data.getBookingType())
                .build();

        bookingService.createBooking(booking);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}")
    public void updateRoute(@PathVariable(name = "passengerId") Long passengerId,
                            @PathVariable(name = "bookingId") Long bookingId,
                            @RequestBody Booking data) {
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        List<ExactLocation> route = new ArrayList<>(booking.getCompletedRoute());
        data.getRoute().forEach(location -> route.add(ExactLocation.builder()
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .build()));
        bookingService.updateRoute(booking, route);
    }

    @DeleteMapping("{passengerId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "passengerId") Long passengerId, @PathVariable(name = "bookingId") Long bookingId){
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        bookingService.cancelByPassenger(passenger, booking);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}/start")
    public void startRide(@PathVariable(name = "passengerId") Long passengerId, @PathVariable(name = "bookingId") Long bookingId, @RequestBody OTP otp){
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        booking.startRide(otp, constants.getRideStartOTPExpiryMinutes());
        bookingRepository.save(booking);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}/end")
    public void endRide(@PathVariable(name = "passengerId") Long passengerId, @PathVariable(name = "bookingId") Long bookingId){
        Passenger passenger = getPassengerFromId(passengerId);
        Booking booking = getPassengerBookingFromId(bookingId, passenger);
        booking.endRide();
        bookingRepository.save(booking);
    }

    @PatchMapping("{passengerId}/bookings/{bookingId}/rate")
    public void rateRide(@PathVariable(name = "passengerId") Long passengerId, @PathVariable(name = "bookingId") Long bookingId, @RequestBody Review data){
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
