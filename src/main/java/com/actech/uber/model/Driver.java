package com.actech.uber.model;

import com.actech.uber.exception.UnapprovedDriverException;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Driver is a special account - not inheritance
// Composition - Driver has an account
// Passenger has an account
// modeled as inheritance
// table-per-class
// single-table
// no-parent-table
// join-table

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "driver")
public class Driver extends Auditable{
    @OneToOne
    private Account account;

    private Gender gender;
    private String  name;

    private String phoneNumber;

    @OneToOne
    private Review avgRating; // will be updated by a nightly cron job

    @OneToOne(mappedBy = "driver")
    private Car car;

    private String licenseDetails;

    @Temporal(value = TemporalType.DATE)
    private Date dob;

    @Enumerated(value = EnumType.STRING)
    private DriverApprovalStatus driverApprovalStatus;

    @OneToMany(mappedBy = "driver")
    private List<Booking> bookings;

    @ManyToMany(mappedBy = "notifiedDrivers", cascade = CascadeType.PERSIST)
    private Set<Booking> acceptableBookings = new HashSet<>(); // bookings that the driver can currently accept

    @OneToOne
    private Booking activeBooking = null;  // driver.active_booking_id  either be null or be a foreign key


    private Boolean isAvailable;

    private String activeCity;

    @OneToOne
    private ExactLocation lastKnownLocation;

    @OneToOne
    private ExactLocation home;

    public void setAvailable(Boolean available) {
        if(available && !driverApprovalStatus.equals(DriverApprovalStatus.APPROVED))
            throw new UnapprovedDriverException("Driver approval pending or denied "+getId());
        isAvailable = available;
    }
    public boolean canAcceptBooking(int maxWaitTimeForPreviousRide) {
        if (isAvailable && activeBooking == null) {
            return true;
        }
        return activeBooking.getExpectedCompletionTime().before(
                DateUtils.addMinutes(new Date(), maxWaitTimeForPreviousRide));
    }
}
