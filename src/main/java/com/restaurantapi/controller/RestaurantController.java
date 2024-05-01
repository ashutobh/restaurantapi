package com.restaurantapi.controller;

import com.restaurantapi.request.RestaurantRequest;
import com.restaurantapi.request.UserRequest;
import com.restaurantapi.response.*;
import com.restaurantapi.service.RestaurantService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant/api/v1")
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/addUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(restaurantService.createUser(userRequest));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/showAllUsers", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseList> showAllUsers() {
        return ResponseEntity.ok(restaurantService.showAllUsers());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/createSession/{sessionRequestedBy}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SessionResponse> createSession(@PathVariable String sessionRequestedBy) {
        return ResponseEntity.ok(restaurantService.createSession(sessionRequestedBy));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/joinSession/{sessionId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> joinSession(@PathVariable(value = "sessionId") Long sessionId, @PathVariable(value = "userId") String userEmailAddress) {
        return ResponseEntity.ok(restaurantService.joinSession(sessionId, userEmailAddress));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/getActiveSession", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SessionResponse> getActiveSession() {
        return ResponseEntity.ok(restaurantService.getActiveSession());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/showAllSessionUsers/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseList> showAllSessionUsers(@PathVariable(value = "sessionId") Long sessionId) {
        return ResponseEntity.ok(restaurantService.showAllSessionUsers(sessionId));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/addRestaurant/{sessionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> addRestaurant(@RequestBody RestaurantRequest restaurantRequest, @PathVariable(value = "sessionId") Long sessionId) {
        return ResponseEntity.ok(restaurantService.addRestaurant(restaurantRequest, sessionId));
    }


    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/getSelectedRestaurant", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantResponse> getSelectedRestaurant() {
        return ResponseEntity.ok(restaurantService.getSelectedRestaurant());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(value = "/terminateSession/{sessionId}/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RestaurantResponse> terminateSession(@PathVariable(value = "sessionId") Long sessionId, @PathVariable(value = "userId") String userEmailAddress) {
        return ResponseEntity.ok(restaurantService.terminateSession(sessionId, userEmailAddress));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(value = "/resetToDefault", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> resetToDefault() {
        return ResponseEntity.ok(restaurantService.resetToDefault());
    }
}

