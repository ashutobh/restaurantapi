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
@Table(name = "TBL_RESTAURANT")
public class Restaurant {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "RESTAURANT_NAME")
    private String restaurantName;

    @Column(name = "RESTAURANT_ADDRESS")
    private String restaurantAddress;

    @Column(name = "ADDED_BY")
    private String addedBy;

    @Column(name = "SESSION_ID")
    private Long sessionId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;
}
