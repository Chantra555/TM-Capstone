package com.muro.repository;

import com.muro.entity.Trip;
import com.muro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByUser(User user);

    Optional<Trip> findByIdAndUser(Long id, User user);
}
