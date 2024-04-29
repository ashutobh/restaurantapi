package com.restaurantapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class RestaurantResponse {
    private String restaurantName;
    private String restaurantAddedBy;
    private String restaurantStatus;
    private String sessionStatus;
    private Long sessionId;
    private int statusCode;
    private String message;
}
