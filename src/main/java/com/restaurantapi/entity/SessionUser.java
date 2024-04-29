package com.restaurantapi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_SESSION_USER")
public class SessionUser {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "USER_EMAIL_ADDRESS")
    private String userEmailAddress;

    @Column(name = "SESSION_ID")
    private Long sessionId;
}