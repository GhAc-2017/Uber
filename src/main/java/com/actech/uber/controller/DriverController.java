package com.actech.uber.controller;

import com.actech.uber.exception.InvalidBookingException;
import com.actech.uber.model.Booking;
import com.actech.uber.model.Driver;
import com.actech.uber.model.OTP;
import com.actech.uber.model.Review;
import com.actech.uber.repositories.BookingRepository;
import com.actech.uber.repositories.DriverRepository;
import com.actech.uber.repositories.ReviewRepository;
import com.actech.uber.services.BookingService;
import com.actech.uber.services.Constants;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.actech.uber.utils.UtilityProcessor.getDriverFromId;

@RequestMapping("/driver")
@RestController
public class DriverController {
    final DriverRepository driverRepository;
    final BookingRepository bookingRepository;
    final ReviewRepository reviewRepository;
    final BookingService bookingService;
    final Constants constants;

    public DriverController(DriverRepository driverRepository, BookingRepository bookingRepository, ReviewRepository reviewRepository, BookingService bookingService, Constants constants) {
        this.driverRepository = driverRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.bookingService = bookingService;
        this.constants = constants;
    }


    public Booking getDriverBookingFromId(Long bookingId, Driver driver){
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if(optionalBooking.isEmpty())
            throw new InvalidBookingException("No booking with id "+ optionalBooking);
        Booking booking = optionalBooking.get();
        if(!booking.getDriver().equals(driver))
            throw new InvalidBookingException("Driver "+driver.getBookings() + " has no such booking "+bookingId);
            return booking;
    }

    @GetMapping("/{driverId}")
    public Driver getDriverDetails(@PathVariable(name = "driverId") Long driverId){
        return getDriverFromId(driverId);
    }

    @PatchMapping("/{driverId}")
    public void changeAvailability(@PathVariable(name = "driverId") Long driverId, @RequestBody Boolean available){
        Driver driver = getDriverFromId(driverId);
        driver.setIsAvailable(available);
        driverRepository.save(driver);
    }

    @GetMapping("{driverId}/bookings")
    public List<Booking> getAllBookings(@PathVariable(name = "driverId") Long driverId) {
        Driver driver = getDriverFromId(driverId);
        return driver.getBookings();
    }

    @GetMapping("{driverId}/bookings/{bookingId}")
    public Booking getBooking(@PathVariable(name = "driverId") Long driverId,
                              @PathVariable(name = "bookingId") Long bookingId) {
        Driver driver = getDriverFromId(driverId);
        return getDriverBookingFromId(bookingId, driver);
    }

    @PostMapping("{driverId}/bookings/{bookingId}")
    public void acceptBooking(@PathVariable(name = "driverId") Long driverId, @PathVariable(name = "bookingId") Long bookingId){
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        bookingService.acceptBooking(driver, booking);
    }

    @DeleteMapping("{driverId}/bookings/{bookingId}")
    public void cancelBooking(@PathVariable(name = "driverId") Long driverId, @PathVariable(name = "bookingId") Long bookingId){
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        bookingService.cancelByDriver(driver, booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/start")
    public void startRide(@PathVariable(name = "driverId") Long driverId, @PathVariable(name = "bookingId") Long bookingId, @RequestBody OTP otp){
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        booking.startRide(otp, constants.getRideStartOTPExpiryMinutes());
        bookingRepository.save(booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/end")
    public void endRide(@PathVariable(name = "driverId") Long driverId, @PathVariable(name = "bookingId") Long bookingId){
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        booking.endRide();
        bookingRepository.save(booking);
    }

    @PatchMapping("{driverId}/bookings/{bookingId}/rate")
    public void rateRide(@PathVariable(name = "driverId") Long driverId, @PathVariable(name = "bookingId") Long bookingId, @RequestBody Review data){
        Driver driver = getDriverFromId(driverId);
        Booking booking = getDriverBookingFromId(bookingId, driver);
        Review review = Review.builder()
                .note(data.getNote())
                .ratingOutOfFive(data.getRatingOutOfFive())
                .build();
        booking.setReviewByDriver(review);
        reviewRepository.save(review);
        bookingRepository.save(booking);
    }
}
