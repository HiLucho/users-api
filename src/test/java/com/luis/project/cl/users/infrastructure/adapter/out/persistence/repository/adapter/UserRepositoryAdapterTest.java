package com.luis.project.cl.users.infrastructure.adapter.out.persistence.repository.adapter;

import com.luis.project.cl.users.application.ports.UserRepositoryPort;
import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.request.PhoneRequest;
import com.luis.project.cl.users.domain.request.UserRequest;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.PhoneDto;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.UserDto;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.mapper.UserMapper;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.repository.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryAdapterTest {
    private static final UUID DEFAULT_UUID = UUID.fromString("f3bfb949-e58e-46bd-94e5-8a0ddc6a1c5f");
    private UserRepositoryPort userRepositoryPort;
    @Mock
    private UserRepository userRepository;
    private UserDto userDto;
    private User user;
    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime localDateTime = LocalDateTime.now();
        userRepository = Mockito.mock(UserRepository.class);
        userRepositoryPort = new UserRepositoryAdapter(userRepository);

        PhoneDto phoneDto = PhoneDto.builder()
                .countryCode("0")
                .number("123")
                .cityCode("1")
                .build();

        PhoneRequest phoneRequest = PhoneRequest.builder().countryCode("0")
                .number("123")
                .cityCode("1").build();
        userRequest = UserRequest.builder()
                .name("name")
                .email("some@dominio.cl")
                .password("123")
                .phones(List.of(phoneRequest)).build();


        userDto = UserDto.builder()
                .email("some@dominio.cl")
                .id(DEFAULT_UUID)
                .password("123")
                .created(localDateTime)
                .lastLogin(localDateTime)
                .fullName("name")
                .phones(List.of(phoneDto))
                .isActive(true)
                .build();


        user = UserMapper.INSTANCE.dtoToModel(userDto);
    }

    @Test
    void findById() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userDto));
        var response = this.userRepositoryPort.findById(DEFAULT_UUID);

        assertThat(response).isPresent();
        assertThat(response.get().getEmail()).isEqualTo("some@dominio.cl");
    }


    @Test
    void saveUser() {
        when(userRepository.save(any())).thenReturn(userDto);

        var response = this.userRepositoryPort.save(userRequest);

        assertThat(response).isPresent();
        assertThat(response.get().isActive()).isTrue();
    }

    @Test
    void updateUser() {
        when(userRepository.save(any())).thenReturn(userDto);

        var response = this.userRepositoryPort.update(userRequest, user);

        assertThat(response).isPresent();
        assertThat(response.get().isActive()).isTrue();
    }
}