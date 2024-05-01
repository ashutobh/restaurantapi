package com.restaurantapi.service;

import com.restaurantapi.request.RestaurantRequest;
import com.restaurantapi.request.UserRequest;
import com.restaurantapi.response.*;

public interface RestaurantService {

    UserResponse createUser(UserRequest userRequest);

    UserResponseList showAllUsers();

    SessionResponse createSession(String sessionRequestedBy);

    Response joinSession(Long sessionId, String userEmailAddress);

    SessionResponse getActiveSession();

    UserResponseList showAllSessionUsers(Long sessionId);

    Response addRestaurant(RestaurantRequest restaurantRequest, Long sessionId);

    RestaurantResponse getSelectedRestaurant();

    RestaurantResponse terminateSession(Long sessionId, String userEmailAddress);
    Response resetToDefault();
}
