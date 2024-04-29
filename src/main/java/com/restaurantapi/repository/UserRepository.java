package com.restaurantapi.repository;


import com.restaurantapi.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDetails, String> {
    List<UserDetails> findByEmailAddress(String userEmailAddress);
}