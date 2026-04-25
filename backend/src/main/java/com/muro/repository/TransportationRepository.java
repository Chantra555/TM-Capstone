package com.muro.repository;

import com.muro.entity.Transportation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransportationRepository extends JpaRepository<Transportation, Long> {

    List<Transportation> findByTripId(Long tripId);
}
