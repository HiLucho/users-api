package com.luis.project.cl.users.application.services.impl;


import com.luis.project.cl.users.application.ports.UserRepositoryPort;
import com.luis.project.cl.users.application.services.UserService;
import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.exception.UserAlreadyExistsException;
import com.luis.project.cl.users.domain.exception.UserApiException;
import com.luis.project.cl.users.domain.exception.UserNotFoundException;
import com.luis.project.cl.users.domain.request.UserRequest;
import com.luis.project.cl.users.domain.response.UserCreated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepositoryPort userPersistencePort;

    @Override
    public UserCreated createUser(UserRequest userRequest) {

        userPersistencePort.findByEmail(userRequest.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("Email " + u.getEmail() + " already exists");
        });

        return userPersistencePort.save(userRequest).orElseThrow(() -> new UserApiException(String.format("Error creating an user %s", userRequest)));

    }

    @Override
    public User getUser(UUID id) {
        return userPersistencePort.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
    }

    @Override
    public UserCreated updateUser(UserRequest userRequest, UUID id) {
        var userExist = userPersistencePort.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
        return userPersistencePort.update(userRequest, userExist).orElseThrow(() -> new UserApiException(String.format("Error updating an user %s", userRequest)));
    }

    @Override
    public void deactivateUser(UUID id) {
        var userExist = userPersistencePort.findById(id).orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
        userPersistencePort.deactivateUser(userExist);
    }

}