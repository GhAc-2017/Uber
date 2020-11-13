package com.actech.uber.repositories;

import com.actech.uber.model.NamedLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NamedLocationRepository extends JpaRepository<NamedLocation, Long> {
}
