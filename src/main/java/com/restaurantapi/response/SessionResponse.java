package com.restaurantapi.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class SessionResponse {
    private Long sessionId;
    private String sessionRequestedBy;
    private int statusCode;
    private String message;
}
