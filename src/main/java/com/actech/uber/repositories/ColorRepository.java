package com.actech.uber.repositories;

import com.actech.uber.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long>  {
}
