package com.restaurantapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantapi.request.RestaurantRequest;
import com.restaurantapi.request.UserRequest;
import com.restaurantapi.response.*;
import com.restaurantapi.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = { "spring.config.location=classpath:application-test.yml" })
class RestaurantControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private RestaurantService restaurantService;
    @Autowired
    private ObjectMapper objectMapper;

    private UserResponse userResponse;
    private SessionResponse sessionResponse;
    private Response response;
    private RestaurantResponse restaurantResponse;

    @BeforeEach
    public void setUp() {
        userResponse = UserResponse.builder()
                .firstName("Ashutosh")
                .lastName("Bhardwaj")
                .emailAddress("ashutosh0313@gmail.com")
                .statusCode(HttpStatus.CREATED.value())
                .build();

        sessionResponse = SessionResponse.builder()
                .sessionId(1L)
                .sessionRequestedBy("Ashutosh")
                .statusCode(HttpStatus.OK.value())
                .build();

        response = Response.builder()
                .message("SUCCESS")
                .statusCode(HttpStatus.OK.value())
                .build();

        restaurantResponse = RestaurantResponse.builder()
                .restaurantName("Banana Leaf")
                .restaurantAddedBy("Ashutosh0313@gmail.com")
                .restaurantStatus("ACTIVE")
                .sessionStatus("ACTIVE")
                .sessionId(1L)
                .statusCode(HttpStatus.OK.value())
                .message("SUCCESS")
                .build();
    }

    @Test
    void createUserTest() throws Exception {
        Mockito.when(restaurantService.createUser(Mockito.any())).thenReturn(userResponse);
        mvc.perform(MockMvcRequestBuilders
                        .post("/restaurant/api/v1/addUser")
                        .content(objectMapper.writeValueAsString(UserRequest.builder().userFirstName("Ashutosh").userLastName("Bhardwaj").build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").exists());
    }

    @Test
    void showAllUsersTest() throws Exception {
        Mockito.when(restaurantService.showAllUsers()).thenReturn(UserResponseList.builder().users(List.of(userResponse)).statusCode(HttpStatus.OK.value()).build());
        mvc.perform(MockMvcRequestBuilders
                        .get("/restaurant/api/v1/showAllUsers")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[0].firstName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[0].lastName").isNotEmpty());
    }

    @Test
    void createSessionTest() throws Exception {
        Mockito.when(restaurantService.createSession(Mockito.any())).thenReturn(sessionResponse);
        mvc.perform(MockMvcRequestBuilders
                        .post("/restaurant/api/v1/createSession/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sessionId").isNotEmpty());
    }

    @Test
    void joinSessionTest() throws Exception {
        Mockito.when(restaurantService.joinSession(Mockito.anyLong(), Mockito.anyString())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders
                        .post("/restaurant/api/v1/joinSession/1/ashutosh0313@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
    }

    @Test
    void getActiveSessionTest() throws Exception {
        Mockito.when(restaurantService.getActiveSession()).thenReturn(sessionResponse);
        mvc.perform(MockMvcRequestBuilders
                        .get("/restaurant/api/v1/getActiveSession")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sessionId").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sessionRequestedBy").isNotEmpty());
    }

    @Test
    void showAllSessionUsersTest() throws Exception {
        Mockito.when(restaurantService.showAllSessionUsers(Mockito.anyLong())).thenReturn(UserResponseList.builder().users(List.of(userResponse)).statusCode(HttpStatus.OK.value()).build());
        mvc.perform(MockMvcRequestBuilders
                        .get("/restaurant/api/v1/showAllSessionUsers/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[0].firstName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.users[0].lastName").isNotEmpty());
    }

    @Test
    void addRestaurantTest() throws Exception {
        Mockito.when(restaurantService.addRestaurant(Mockito.any(), Mockito.anyLong())).thenReturn(response);
        mvc.perform(MockMvcRequestBuilders
                        .post("/restaurant/api/v1/addRestaurant/1")
                        .content(objectMapper.writeValueAsString(RestaurantRequest.builder().restaurantName("Banana Leaf").restaurantAddress("Sengkang").build()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
    }

    @Test
    void getSelectedRestaurantTest() throws Exception {
        Mockito.when(restaurantService.getSelectedRestaurant()).thenReturn(restaurantResponse);
        mvc.perform(MockMvcRequestBuilders
                        .get("/restaurant/api/v1/getSelectedRestaurant")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.restaurantName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.restaurantAddedBy").isNotEmpty());
    }

    @Test
    void terminateSessionTest() throws Exception {
        Mockito.when(restaurantService.terminateSession(Mockito.anyLong(), Mockito.anyString())).thenReturn(restaurantResponse);
        mvc.perform(MockMvcRequestBuilders
                        .post("/restaurant/api/v1/terminateSession/1/ashutosh0313@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.restaurantName").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.restaurantAddedBy").isNotEmpty());
    }

}
