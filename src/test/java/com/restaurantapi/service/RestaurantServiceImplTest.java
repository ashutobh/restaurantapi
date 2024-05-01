package com.restaurantapi.service;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @InjectMocks
    private RestaurantServiceImpl restaurantServiceImpl;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private SessionUserRepository sessionUserRepository;
    @Mock
    private UserRepository userRepository;

    private UserDetails userDetails;
    private Session session;
    private SessionUser sessionUser;
    private Restaurant restaurant;

    @BeforeEach
    public void setup() {
        userDetails = UserDetails.builder()
                .firstName("Ashutosh")
                .lastName("Bhardwaj")
                .emailAddress("ashutosh0313@gmail.com")
                .build();

        session = Session.builder()
                .sessionId(1L)
                .sessionStatus("ACTIVE")
                .build();

        sessionUser = SessionUser.builder()
                .id(1L)
                .userEmailAddress("ashutosh0313@gmail.com")
                .sessionId(1L)
                .build();

        restaurant = Restaurant.builder()
                .restaurantName("Banana Leaf")
                .sessionId(1L)
                .addedBy("ashutosh0313@gmail.com")
                .build();
    }

    @Test
    void createUserWhenSuccessTest() {
        Mockito.when(userRepository.findByEmailAddress(Mockito.anyString())).thenReturn(Collections.emptyList());
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(userDetails);
        var user = restaurantServiceImpl.createUser(UserRequest.builder()
                .userFirstName("Ashutosh")
                .userLastName("Bhardwaj")
                .userEmailAddress("ashutosh0313@gmail.com")
                .build());
        Assertions.assertEquals("Ashutosh", user.getFirstName());
        Assertions.assertEquals("Bhardwaj", user.getLastName());
        Assertions.assertEquals("ashutosh0313@gmail.com", user.getEmailAddress());
    }

    @Test
    void createUserWhenUserAlreadyExistsTest() {
        Mockito.when(userRepository.findByEmailAddress(Mockito.anyString())).thenReturn(List.of(userDetails));
        var user = restaurantServiceImpl.createUser(UserRequest.builder()
                .userFirstName("Ashutosh")
                .userLastName("Bhardwaj")
                .userEmailAddress("ashutosh0313@gmail.com")
                .build());
        Assertions.assertEquals("User already exists!", user.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), user.getStatusCode());
    }

    @Test
    void showAllUsersWhenSuccessTest() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(userDetails));
        var users = restaurantServiceImpl.showAllUsers();
        Assertions.assertEquals("Ashutosh", users.getUsers().get(0).getFirstName());
        Assertions.assertEquals("Bhardwaj", users.getUsers().get(0).getLastName());
        Assertions.assertEquals("ashutosh0313@gmail.com", users.getUsers().get(0).getEmailAddress());
    }

    @Test
    void showAllUsersWhenNotFoundTest() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());
        var users = restaurantServiceImpl.showAllUsers();
        Assertions.assertEquals("No User Found!", users.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), users.getStatusCode());
    }

    @Test
    void createSessionWhenUserNotFoundTest() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        var response = restaurantServiceImpl.createSession("ashutosh0313@gmail.com");
        Assertions.assertEquals("User must be added before creating the session!", response.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void createSessionWhenActiveSessionSessionFoundTest() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(userDetails));
        Mockito.when(sessionRepository.findBySessionStatus(Mockito.anyString())).thenReturn(List.of(session));
        var response = restaurantServiceImpl.createSession("ashutosh0313@gmail.com");
        Assertions.assertEquals("One Active session is already present. Please invalidate the active session if you want to create new session", response.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void createSessionWhenNoActiveSessionFoundTest() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(userDetails));
        Mockito.when(sessionRepository.findBySessionStatus(Mockito.anyString())).thenReturn(Collections.emptyList());
        Mockito.when(sessionRepository.save(Mockito.any())).thenReturn(session);
        var response = restaurantServiceImpl.createSession("ashutosh0313@gmail.com");
        Assertions.assertEquals("Success", response.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void joinSessionWhenUserNotFoundTest() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
        var response = restaurantServiceImpl.joinSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("User must be added before joining to session!", response.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void joinSessionWhenInvalidSessionIdProvidedTest() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(userDetails));
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        var response = restaurantServiceImpl.joinSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("Invalid session provided!", response.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void joinSessionWhenProvidedSessionIsNotActiveTest() {
        session.setSessionStatus("INVALIDATED");
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(userDetails));
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        var response = restaurantServiceImpl.joinSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("Session is already Invalidated!", response.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void joinSessionWhenUserAlreadyJoinedTest() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(userDetails));
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        Mockito.when(sessionUserRepository.findByUserEmailAddressAndSessionId(Mockito.anyString(), Mockito.anyLong())).thenReturn(List.of(sessionUser));
        var response = restaurantServiceImpl.joinSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("User has already joined the session!", response.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    void joinSessionWhenSuccessTest() {
        Mockito.when(userRepository.findById(Mockito.anyString())).thenReturn(Optional.of(userDetails));
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        Mockito.when(sessionUserRepository.findByUserEmailAddressAndSessionId(Mockito.anyString(), Mockito.anyLong())).thenReturn(Collections.emptyList());
        Mockito.when(sessionUserRepository.save(Mockito.any())).thenReturn(sessionUser);
        var response = restaurantServiceImpl.joinSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("Success", response.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    void getActiveSessionWhenSuccessTest() {
        Mockito.when(sessionRepository.findBySessionStatus(Mockito.anyString())).thenReturn(List.of(session));
        var activeSession = restaurantServiceImpl.getActiveSession();
        Assertions.assertEquals(1L, activeSession.getSessionId());
        Assertions.assertEquals(HttpStatus.OK.value(), activeSession.getStatusCode());
    }

    @Test
    void getActiveSessionWhenNoSessionFoundTest() {
        Mockito.when(sessionRepository.findBySessionStatus(Mockito.anyString())).thenReturn(Collections.emptyList());
        var activeSession = restaurantServiceImpl.getActiveSession();
        Assertions.assertEquals("There is no active session!", activeSession.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), activeSession.getStatusCode());
    }

    @Test
    void showAllSessionUsersWhenInvalidSessionTest() {
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        var users = restaurantServiceImpl.showAllSessionUsers(1L);
        Assertions.assertEquals("Invalid session", users.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), users.getStatusCode());
    }

    @Test
    void showAllSessionUsersWhenInvalidUserTest() {
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        Mockito.when(sessionUserRepository.findBySessionId(Mockito.anyLong())).thenReturn(Collections.emptyList());
        var users = restaurantServiceImpl.showAllSessionUsers(1L);
        Assertions.assertEquals("No User Found in session!", users.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), users.getStatusCode());
    }

    @Test
    void showAllSessionUsersWhenSuccessTest() {
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        Mockito.when(sessionUserRepository.findBySessionId(Mockito.anyLong())).thenReturn(List.of(sessionUser));
        var users = restaurantServiceImpl.showAllSessionUsers(1L);
        Assertions.assertEquals("Success", users.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), users.getStatusCode());
    }

    @Test
    void addRestaurantWhenNoActiveSessionTest() {
        Mockito.when(sessionUserRepository.findByUserEmailAddressAndSessionId(Mockito.anyString(), Mockito.anyLong())).thenReturn(List.of(sessionUser));
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        var users = restaurantServiceImpl.addRestaurant(RestaurantRequest.builder()
                .restaurantName("Banana Leaf")
                .restaurantAddress("Sengkang")
                .restaurantAddedByEmail("ashutosh0313@gmail.com")
                .build(), 1L);
        Assertions.assertEquals("There is no active session!", users.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), users.getStatusCode());
    }

    @Test
    void addRestaurantWhenOnlyOneRequestPermittedTest() {
        Mockito.when(sessionUserRepository.findByUserEmailAddressAndSessionId(Mockito.anyString(), Mockito.anyLong())).thenReturn(List.of(sessionUser));
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        Mockito.when(restaurantRepository.findBySessionIdAndAddedBy(Mockito.anyLong(), Mockito.anyString())).thenReturn(Optional.of(restaurant));
        var users = restaurantServiceImpl.addRestaurant(RestaurantRequest.builder()
                .restaurantName("Banana Leaf")
                .restaurantAddress("Sengkang")
                .restaurantAddedByEmail("ashutosh0313@gmail.com")
                .build(), 1L);
        Assertions.assertEquals("One Request permitted for one user/session!", users.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), users.getStatusCode());
    }

    @Test
    void addRestaurantWhenSuccessTest() {
        Mockito.when(sessionUserRepository.findByUserEmailAddressAndSessionId(Mockito.anyString(), Mockito.anyLong())).thenReturn(List.of(sessionUser));
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        Mockito.when(restaurantRepository.findBySessionIdAndAddedBy(Mockito.anyLong(), Mockito.anyString())).thenReturn(Optional.empty());
        var users = restaurantServiceImpl.addRestaurant(RestaurantRequest.builder()
                .restaurantName("Banana Leaf")
                .restaurantAddress("Sengkang")
                .restaurantAddedByEmail("ashutosh0313@gmail.com")
                .build(), 1L);
        Assertions.assertEquals("Success", users.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), users.getStatusCode());
    }

    @Test
    void getSelectedRestaurantWhenNoRestaurantFoundTest() {
        Mockito.when(sessionRepository.findAll()).thenReturn(Collections.emptyList());
        var users = restaurantServiceImpl.getSelectedRestaurant();
        Assertions.assertEquals("Restaurant Detail not found!", users.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), users.getStatusCode());
    }

    @Test
    void getSelectedRestaurantWhenSuccessTest() {
        session.setSelectedRestaurantId(1L);
        Mockito.when(sessionRepository.findAll()).thenReturn(List.of(session));
        Mockito.when(restaurantRepository.findById(Mockito.any())).thenReturn(Optional.of(restaurant));
        var users = restaurantServiceImpl.getSelectedRestaurant();
        Assertions.assertEquals("Success", users.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), users.getStatusCode());
    }

    @Test
    void terminateSessionWhenNoSessionFoundTest() {
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        var users = restaurantServiceImpl.terminateSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("No Session Found!", users.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), users.getStatusCode());
    }

    @Test
    void terminateSessionWhenNoActiveSessionFoundTest() {
        session.setSessionStatus("INACTIVE");
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        var users = restaurantServiceImpl.terminateSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("No Active Session Found!", users.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), users.getStatusCode());
    }

    @Test
    void terminateSessionWhenNoRestaurantFoundTest() {
        session.setCreatedBy("ashutosh0313@gmail.com");
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        Mockito.when(restaurantRepository.findBySessionId(Mockito.any())).thenReturn(Collections.emptyList());
        var users = restaurantServiceImpl.terminateSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("AtLeast one restaurant must be added!", users.getMessage());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), users.getStatusCode());
    }

    @Test
    void terminateSessionWhenSelectRandomlyOneTest() {
        session.setCreatedBy("ashutosh0313@gmail.com");
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        Mockito.when(restaurantRepository.findBySessionId(Mockito.any())).thenReturn(List.of(restaurant, restaurant));
        var users = restaurantServiceImpl.terminateSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("Success", users.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), users.getStatusCode());
    }

    @Test
    void terminateSessionWhenSuccessTest() {
        session.setCreatedBy("ashutosh0313@gmail.com");
        Mockito.when(sessionRepository.findById(Mockito.any())).thenReturn(Optional.of(session));
        Mockito.when(restaurantRepository.findBySessionId(Mockito.any())).thenReturn(List.of(restaurant));
        var users = restaurantServiceImpl.terminateSession(1L, "ashutosh0313@gmail.com");
        Assertions.assertEquals("Success", users.getMessage());
        Assertions.assertEquals(HttpStatus.OK.value(), users.getStatusCode());
    }

}
