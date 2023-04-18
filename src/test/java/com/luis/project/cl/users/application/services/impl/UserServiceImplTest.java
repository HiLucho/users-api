package com.luis.project.cl.users.application.services.impl;

import com.luis.project.cl.users.application.ports.UserRepositoryPort;
import com.luis.project.cl.users.application.services.UserService;
import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.exception.UserAlreadyExistsException;
import com.luis.project.cl.users.domain.request.PhoneRequest;
import com.luis.project.cl.users.domain.request.UserRequest;
import com.luis.project.cl.users.domain.response.UserCreated;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.PhoneDto;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.UserDto;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.mapper.UserMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static final UUID DEFAULT_UUID = UUID.fromString("f3bfb949-e58e-46bd-94e5-8a0ddc6a1c5f");
    @Mock
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private UserService service;
    private UserRequest userRequest;
    private UserCreated userCreated;
    private User user;

    @BeforeEach
    void setUp() {

        userRepositoryPort = Mockito.mock(UserRepositoryPort.class);

        service = new UserServiceImpl(userRepositoryPort);
        LocalDateTime localDateTime = LocalDateTime.now();

        PhoneRequest phoneRequest = PhoneRequest.builder().countryCode("0")
                .number("123")
                .cityCode("1").build();
        userRequest = UserRequest.builder()
                .name("name")
                .email("some@dominio.cl")
                .password("123")
                .phones(List.of(phoneRequest)).build();

        PhoneDto phoneDto = PhoneDto.builder()
                .countryCode("0")
                .number("123")
                .cityCode("1")
                .build();

        UserDto userDto = UserDto.builder()
                .email("some@dominio.cl")
                .id(DEFAULT_UUID)
                .password("123")
                .created(localDateTime)
                .lastLogin(localDateTime)
                .fullName("name")
                .phones(List.of(phoneDto))
                .isActive(true)
                .build();
        userCreated = UserCreated.builder().created(localDateTime).id(DEFAULT_UUID).isActive(true).token(DEFAULT_UUID).lastLogin(localDateTime).build();

        user = UserMapper.INSTANCE.dtoToModel(userDto);
    }

    @Test
    void updateUserSuccessfully() {

        when(this.userRepositoryPort.findById(any())).thenReturn(Optional.of(user));
        when(this.userRepositoryPort.update(any(), any())).thenReturn(Optional.of(userCreated));

        service.updateUser(userRequest, DEFAULT_UUID);

        verify(this.userRepositoryPort).findById(DEFAULT_UUID);
    }

    @Test
    void createUserSuccessfully() {

        when(this.userRepositoryPort.findByEmail(any())).thenReturn(Optional.empty());
        when(this.userRepositoryPort.save(any())).thenReturn(Optional.of(userCreated));

        service.createUser(userRequest);

        verify(this.userRepositoryPort).findByEmail("some@dominio.cl");
    }

    @Test
    void createUserError() {

        when(this.userRepositoryPort.findByEmail(any())).thenReturn(Optional.of(user));

        Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            service.createUser(userRequest);
        });

        verify(this.userRepositoryPort).findByEmail("some@dominio.cl");
    }


}