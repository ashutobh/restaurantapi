package com.restaurantapi.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantRequest {
    private String restaurantName;
    private String restaurantAddress;
    private String restaurantAddedByEmail;
}
