package com.restaurantapi.service;

import com.restaurantapi.constant.RestaurantConstant;
import com.restaurantapi.constant.SessionState;
import com.restaurantapi.entity.Restaurant;
import com.restaurantapi.entity.Session;
import com.restaurantapi.entity.SessionUser;
import com.restaurantapi.entity.UserDetails;
import com.restaurantapi.repository.RestaurantRepository;
import com.restaurantapi.repository.SessionRepository;
import com.restaurantapi.repository.SessionUserRepository;
import com.restaurantapi.repository.UserRepository;
import com.restaurantapi.request.RestaurantRequest;
import com.restaurantapi.request.UserRequest;
import com.restaurantapi.response.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final SessionRepository sessionRepository;
    private final SessionUserRepository sessionUserRepository;
    private final UserRepository userRepository;
    private static final Random RANDOM = new Random();

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        var user = UserDetails.builder()
                .firstName(userRequest.getUserFirstName())
                .lastName(userRequest.getUserLastName())
                .emailAddress(userRequest.getUserEmailAddress())
                .status(RestaurantConstant.ACTIVE)
                .createdDate(new Date())
                .build();
        var userList = userRepository.findByEmailAddress(userRequest.getUserEmailAddress());
        if (userList.isEmpty()) {
            userRepository.save(user);
            return UserResponse.builder()
                    .firstName(userRequest.getUserFirstName())
                    .lastName(userRequest.getUserLastName())
                    .emailAddress(userRequest.getUserEmailAddress())
                    .statusCode(HttpStatus.CREATED.value())
                    .message(RestaurantConstant.SUCCESS)
                    .build();
        } else {
            return UserResponse.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("User already exists!")
                    .build();
        }
    }

    @Override
    public UserResponseList showAllUsers() {
        var userList = userRepository.findAll();
        var response = userList.stream().map(user -> UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emailAddress(user.getEmailAddress())
                .statusCode(0)
                .message(null)
                .build()).toList();

        if (!response.isEmpty()) {
            return UserResponseList.builder()
                    .users(response)
                    .statusCode(HttpStatus.OK.value())
                    .message(RestaurantConstant.SUCCESS)
                    .build();
        } else {
            return UserResponseList.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("No User Found!")
                    .build();
        }
    }

    @Override
    public SessionResponse createSession(String sessionRequestedBy) {
        var responseBuilder = SessionResponse.builder();
        userRepository.findById(sessionRequestedBy).ifPresentOrElse(user -> {
            var sessionList = sessionRepository.findBySessionStatus(SessionState.ACTIVE.name());
            if (sessionList.isEmpty()) {
                var session = Session.builder()
                        .createdBy(sessionRequestedBy)
                        .startDateTime(new Date())
                        .endDateTime(null)
                        .sessionStatus(SessionState.ACTIVE.name())
                        .selectedRestaurantId(0L)
                        .build();
                var dbsession = sessionRepository.save(session);
                responseBuilder
                        .sessionId(dbsession.getSessionId())
                        .sessionRequestedBy(dbsession.getCreatedBy())
                        .statusCode(HttpStatus.OK.value())
                        .message(RestaurantConstant.SUCCESS)
                        .build();
            } else {
                responseBuilder
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .sessionId(sessionList.get(0).getSessionId())
                        .message("One Active session is already present. Please invalidate the active session if you want to create new session")
                        .build();
            }
        }, () -> responseBuilder
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("User must be added before creating the session!"));

        return responseBuilder.build();
    }

    @Override
    public Response joinSession(Long sessionId, String userEmailAddress) {
        var responseBuilder = Response.builder();
        userRepository.findById(userEmailAddress).ifPresentOrElse(user -> {
            var session = sessionRepository.findById(sessionId);
            if (session.isPresent()) {
                if (session.get().getSessionStatus().equalsIgnoreCase(SessionState.ACTIVE.name())) {
                    var sessionUserList = sessionUserRepository.findByUserEmailAddressAndSessionId(userEmailAddress, sessionId);
                    if(sessionUserList.isEmpty()) {
                        var sessionEntity = SessionUser.builder()
                                .userEmailAddress(userEmailAddress)
                                .sessionId(sessionId)
                                .build();
                        sessionUserRepository.save(sessionEntity);
                        responseBuilder
                                .sessionId(sessionId)
                                .statusCode(HttpStatus.OK.value())
                                .message(RestaurantConstant.SUCCESS)
                                .build();
                    } else {
                        responseBuilder
                                .sessionId(sessionId)
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .message("User has already joined the session!")
                                .build();
                    }

                } else {
                    responseBuilder
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("Session is already Invalidated!")
                            .build();
                }
            } else {
                responseBuilder
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("Invalid session provided!");

            }
        }, () -> responseBuilder
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("User must be added before joining to session!"));

        return responseBuilder.build();
    }

    @Override
    public SessionResponse getActiveSession() {
        var responseBuilder = SessionResponse.builder();
        var sessionList = sessionRepository.findBySessionStatus(SessionState.ACTIVE.name());
        if (!sessionList.isEmpty()) {
            return responseBuilder
                    .sessionId(sessionList.get(0).getSessionId())
                    .sessionRequestedBy(sessionList.get(0).getCreatedBy())
                    .statusCode(HttpStatus.OK.value())
                    .message(RestaurantConstant.SUCCESS)
                    .build();
        } else {
            responseBuilder
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("There is no active session!");
        }
        return responseBuilder.build();
    }

    @Override
    public UserResponseList showAllSessionUsers(Long sessionId) {
        var userListBuilder = UserResponseList.builder();
        sessionRepository.findById(sessionId).ifPresentOrElse(session -> {
            var users = new ArrayList<UserResponse>();
            var sessionUserList = sessionUserRepository.findBySessionId(sessionId);
            if (!sessionUserList.isEmpty()) {
                sessionUserList.forEach(sessionUser -> userRepository.findById(sessionUser.getUserEmailAddress()).ifPresent(user -> users.add(UserResponse.builder()
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .emailAddress(user.getEmailAddress())
                        .statusCode(0)
                        .message(null)
                        .build())));
                userListBuilder
                        .users(users)
                        .statusCode(HttpStatus.OK.value())
                        .message(RestaurantConstant.SUCCESS)
                        .build();
            } else {
                userListBuilder
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("No User Found in session!")
                        .build();
            }
        }, () -> userListBuilder
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(RestaurantConstant.INVALID_SESSION_MSG));

        return userListBuilder.build();
    }

    @Override
    public Response addRestaurant(RestaurantRequest restaurantRequest, Long sessionId) {
        var responseBuilder = Response.builder();
        var sessionUserList = sessionUserRepository.findByUserEmailAddressAndSessionId(restaurantRequest.getRestaurantAddedByEmail(), sessionId);
        sessionUserList.stream()
                .findFirst()
                .ifPresentOrElse(user -> sessionRepository.findById(sessionId)
                        .ifPresentOrElse(session -> restaurantRepository.findBySessionIdAndAddedBy(sessionId, restaurantRequest.getRestaurantAddedByEmail())
                                .ifPresentOrElse(restaurant -> responseBuilder
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("One Request permitted for one user/session!")
                .build(), () -> {
            var restaurant = Restaurant.builder()
                    .restaurantName(restaurantRequest.getRestaurantName())
                    .restaurantAddress(restaurantRequest.getRestaurantAddress())
                    .addedBy(restaurantRequest.getRestaurantAddedByEmail())
                    .sessionId(sessionId)
                    .createdDate(new Date())
                    .build();
            restaurantRepository.save(restaurant);
            responseBuilder
                    .statusCode(HttpStatus.OK.value())
                    .message(RestaurantConstant.SUCCESS)
                    .build();
        }), () -> responseBuilder
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("There is no active session!")), () -> responseBuilder
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message(RestaurantConstant.INVALID_SESSION_MSG));
        return responseBuilder.build();
    }


    @Override
    public RestaurantResponse getSelectedRestaurant() {
        var restaurantBuilder = RestaurantResponse.builder();
        var sessionList = sessionRepository.findAll();
        if (!sessionList.isEmpty()) {
            sessionList.forEach(session -> {
                restaurantBuilder
                        .sessionId(session.getSessionId())
                        .sessionStatus(session.getSessionStatus());

                if (session.getSelectedRestaurantId() == 0) {
                    restaurantBuilder.restaurantStatus("IN PROGRESS");
                } else {
                    restaurantBuilder.restaurantStatus("SELECTED");
                }
                restaurantRepository.findById(session.getSelectedRestaurantId()).ifPresent(restaurant -> restaurantBuilder
                        .restaurantName(restaurant.getRestaurantName())
                        .restaurantAddedBy(restaurant.getAddedBy()));
                restaurantBuilder
                        .message(RestaurantConstant.SUCCESS)
                        .statusCode(HttpStatus.OK.value());
            });
        } else {
            restaurantBuilder
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .message("Restaurant Detail not found!");
        }
        return restaurantBuilder
                .build();
    }

    @Override
    public RestaurantResponse terminateSession(Long sessionId, String userEmailAddress) {
        var restaurantBuilder = RestaurantResponse.builder();
        sessionRepository.findById(sessionId).ifPresentOrElse(session -> {
            if (session.getSessionStatus().equalsIgnoreCase(SessionState.ACTIVE.name())) {
                if (session.getCreatedBy().equalsIgnoreCase(userEmailAddress)) {
                    //Randomly select a restaurant
                    // Need to check if two
                    var restaurantList = restaurantRepository.findBySessionId(sessionId);
                    if(restaurantList.size() >= 2) {
                        var randomRestaurant = restaurantList.get(RANDOM.nextInt(restaurantList.size()));
                        session.setSessionStatus(SessionState.INVALIDATED.name());
                        session.setSelectedRestaurantId(randomRestaurant.getId());
                        session.setEndDateTime(new Date());
                        sessionRepository.save(session);
                        restaurantBuilder
                                .restaurantName(randomRestaurant.getRestaurantName())
                                .restaurantAddedBy(randomRestaurant.getAddedBy())
                                .sessionId(session.getSessionId())
                                .statusCode(HttpStatus.OK.value())
                                .message(RestaurantConstant.SUCCESS);
                    } else if (restaurantList.size() == 1) {
                        session.setSessionStatus(SessionState.INVALIDATED.name());
                        session.setSelectedRestaurantId(restaurantList.get(0).getId());
                        session.setEndDateTime(new Date());
                        sessionRepository.save(session);
                        restaurantBuilder
                                .restaurantName(restaurantList.get(0).getRestaurantName())
                                .restaurantAddedBy(restaurantList.get(0).getAddedBy())
                                .sessionId(session.getSessionId())
                                .statusCode(HttpStatus.OK.value())
                                .message(RestaurantConstant.SUCCESS);
                    } else {
                        restaurantBuilder
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .message("AtLeast one restaurant must be added!")
                                .build();
                    }
                } else {
                    restaurantBuilder
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .message("User Mismatch! User who created session can only terminate!")
                            .build();
                }
            } else {
                restaurantBuilder
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message("No Active Session Found!")
                        .build();
            }
        }, () -> restaurantBuilder
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("No Session Found!")
                .build());
        return restaurantBuilder.build();
    }
}
