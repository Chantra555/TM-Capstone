package com.muro.repository;

import com.muro.entity.Lodging;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LodgingRepository extends JpaRepository<Lodging, Long> {
    List<Lodging> findByTripId(Long tripId);
}
