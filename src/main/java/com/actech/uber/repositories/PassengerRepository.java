package com.actech.uber.repositories;

import com.actech.uber.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findFirstByAccount_Username(String username);
}
