package com.luis.project.cl.users.application.services.impl;


import com.luis.project.cl.users.application.ports.UserPersistencePort;
import com.luis.project.cl.users.application.services.UserService;
import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.exception.TemplateApiException;
import com.luis.project.cl.users.domain.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserPersistencePort userPersistencePort;

    @Override
    public User createUser(User user) {

        userPersistencePort.findByCorreo(user.getEmail()).ifPresent(u -> {
            throw new UserAlreadyExistsException("Email " + u.getEmail() + " already exists");
        });

        return userPersistencePort.save(user).orElseThrow(() -> new TemplateApiException(String.format("Error creating an user %s", user)));
    }

    @Override
    public User getUser(UUID id, UUID token) {
        return null;
    }

    @Override
    public User updateUser(User user, UUID token) {
        return null;
    }

    @Override
    public void deleteUser(UUID id, UUID token) {

    }

    @Override
    public boolean existsUserById(UUID id) {
        return false;
    }
}