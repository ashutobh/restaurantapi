package com.restaurantapi.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserResponse {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private int statusCode;
    private String message;
}
