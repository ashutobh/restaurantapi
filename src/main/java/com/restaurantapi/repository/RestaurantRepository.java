package com.restaurantapi.repository;


import com.restaurantapi.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findBySessionId(Long sessionId);
    Optional<Restaurant> findBySessionIdAndAddedBy(Long sessionId, String restaurantAddedByEmail);
}