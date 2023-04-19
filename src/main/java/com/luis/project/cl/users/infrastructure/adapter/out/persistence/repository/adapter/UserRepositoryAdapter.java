package com.luis.project.cl.users.infrastructure.adapter.out.persistence.repository.adapter;

import com.luis.project.cl.users.application.ports.UserRepositoryPort;
import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.request.UserRequest;
import com.luis.project.cl.users.domain.response.UserCreated;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.PhoneDto;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.UserDto;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.mapper.UserMapper;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * User Repository Service.
 */
@Service
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id).map(UserMapper.INSTANCE::dtoToModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(UserMapper.INSTANCE::dtoToModel);
    }

    @Override
    public Optional<UserCreated> save(UserRequest userRequest) {
        final LocalDateTime now = LocalDateTime.now();

        var userDto = UserDto.builder()
                .created(now)
                .email(userRequest.getEmail())
                .fullName(userRequest.getName())
                .password(userRequest.getPassword())
                .isActive(true)
                .lastLogin(now)
                .token(UUID.randomUUID())
                .build();

        var phonesDto = userRequest.getPhones().stream().map(ph -> PhoneDto.builder()
                        .user(userDto)
                        .countryCode(ph.getCountryCode())
                        .cityCode(ph.getCityCode())
                        .number(ph.getNumber())
                        .build())
                .toList();
        userDto.setPhones(phonesDto);
        return Optional.of(userRepository.save(userDto)).map(UserMapper.INSTANCE::dtoToModelCreated);
    }

    @Override
    @Transactional
    public Optional<UserCreated> update(UserRequest userRequest, User userToUpdate) {
        final LocalDateTime now = LocalDateTime.now();
        var userToUpdateDto = UserMapper.INSTANCE.modelToDto(userToUpdate);
        userToUpdateDto.setModified(now);
        userToUpdateDto.setFullName(userRequest.getName());
        userToUpdateDto.setPassword(userRequest.getPassword());
        userToUpdateDto.setPhones(Collections.emptyList());
        var phonesDto = userRequest.getPhones().stream().map(ph -> PhoneDto.builder()
                        .user(userToUpdateDto)
                        .countryCode(ph.getCountryCode())
                        .cityCode(ph.getCityCode())
                        .number(ph.getNumber())
                        .build())
                .toList();
        userToUpdateDto.setPhones(phonesDto);
        return Optional.of(userRepository.save(userToUpdateDto)).map(UserMapper.INSTANCE::dtoToModelCreated);
    }

    @Override
    public void deactivateUser(User user) {
        var userDto = UserMapper.INSTANCE.modelToDto(user);
        userDto.setActive(false);
        userRepository.save(userDto);
    }
}
