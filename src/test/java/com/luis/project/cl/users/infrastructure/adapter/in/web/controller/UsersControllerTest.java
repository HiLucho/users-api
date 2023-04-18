package com.luis.project.cl.users.infrastructure.adapter.in.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luis.project.cl.users.application.services.UserService;
import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.exception.UserNotFoundException;
import com.luis.project.cl.users.domain.request.PhoneRequest;
import com.luis.project.cl.users.domain.request.UserRequest;
import com.luis.project.cl.users.domain.response.UserCreated;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(UsersController.class)
class UsersControllerTest {
    private static final UUID DEFAULT_UUID = UUID.fromString("f3bfb949-e58e-46bd-94e5-8a0ddc6a1c5f");
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;

    @Test
    void validUUID_returnUserSuccessfully() throws Exception {
        // Mocks & Stubs configuration
        when(userService.getUser(DEFAULT_UUID))
                .thenReturn(User.builder().build());

        // Validating mocks behaviour
        this.mockMvc.perform(get(String.format("/v1/user/%s", DEFAULT_UUID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void invalidEmail_returnBadRequest() throws Exception {
        // Mocks & Stubs configuration
        UserRequest userRequest = UserRequest.builder()
                .email("emal@domain.co")
                .name("Luis")
                .password("1234")
                .phones(List.of(PhoneRequest.builder().cityCode("9").countryCode("56").number("12345678").build()))
                .build();
        UserCreated userCreated = UserCreated.builder().build();
        when(userService.createUser(userRequest))
                .thenReturn(userCreated);

        // Validating mocks behaviour
        this.mockMvc.perform(post("/v1/user/create").content(new ObjectMapper().writeValueAsString(userRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void validEmail_returnSuccess() throws Exception {
        // Mocks & Stubs configuration
        UserRequest userRequest = UserRequest.builder()
                .email("emal@dominio.cl")
                .name("Luis")
                .password("1234")
                .phones(List.of(PhoneRequest.builder().cityCode("9").countryCode("56").number("12345678").build()))
                .build();
        UserCreated userCreated = UserCreated.builder().build();
        when(userService.createUser(userRequest))
                .thenReturn(userCreated);

        // Validating mocks behaviour
        this.mockMvc.perform(post("/v1/user/create").content(new ObjectMapper().writeValueAsString(userRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void validUpdate_returnSuccess() throws Exception {
        // Mocks & Stubs configuration
        UserRequest userRequest = UserRequest.builder()
                .email("emal@dominio.cl")
                .name("Luis")
                .password("1234")
                .phones(List.of(PhoneRequest.builder().cityCode("9").countryCode("56").number("12345678").build()))
                .build();
        UserCreated userCreated = UserCreated.builder().build();
        when(userService.updateUser(userRequest, DEFAULT_UUID))
                .thenReturn(userCreated);

        // Validating mocks behaviour
        this.mockMvc.perform(put(String.format("/v1/user/update/%s", DEFAULT_UUID)).content(new ObjectMapper().writeValueAsString(userRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void NotExistUserUpdate_returnSuccess() throws Exception {
        // Mocks & Stubs configuration
        UserRequest userRequest = UserRequest.builder()
                .email("emal@dominio.cl")
                .name("Luis")
                .password("1234")
                .phones(List.of(PhoneRequest.builder().cityCode("9").countryCode("56").number("12345678").build()))
                .build();
        UserCreated userCreated = UserCreated.builder().build();
        when(userService.updateUser(userRequest, DEFAULT_UUID))
                .thenThrow(UserNotFoundException.class);

        // Validating mocks behaviour
        this.mockMvc.perform(put(String.format("/v1/user/update/%s", DEFAULT_UUID)).content(new ObjectMapper().writeValueAsString(userRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}