package com.restaurantapi.repository;


import com.restaurantapi.entity.SessionUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionUserRepository extends JpaRepository<SessionUser, Long> {
    List<SessionUser> findByUserEmailAddressAndSessionId(String email, Long sessionId);
    List<SessionUser> findBySessionId(Long sessionId);
}
