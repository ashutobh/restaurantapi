package com.restaurantapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_SESSION")
public class Session {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long sessionId;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "START_DATE_TIME")
    private Date startDateTime;

    @Column(name = "END_DATE_TIME")
    private Date endDateTime;

    @Column(name = "SESSION_STATUS")
    private String sessionStatus;

    @Column(name = "SELECTED_RESTAURANT")
    private Long selectedRestaurantId;
}