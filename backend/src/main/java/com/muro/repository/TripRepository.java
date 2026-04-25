package com.muro.repository;

import com.muro.entity.Trip;
import com.muro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByOwner(User owner);

}
