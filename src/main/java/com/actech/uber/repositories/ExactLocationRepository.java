package com.actech.uber.repositories;

import com.actech.uber.model.ExactLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExactLocationRepository extends JpaRepository<ExactLocation, Long> {
}
