package com.luis.project.cl.users.application.ports;

import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.request.UserRequest;
import com.luis.project.cl.users.domain.response.UserCreated;

import java.util.Optional;
import java.util.UUID;

/**
 * User Persistence.
 */
public interface UserRepositoryPort {

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String correo);

    Optional<UserCreated> save(UserRequest userRequest);

    Optional<UserCreated> update(UserRequest userData, User userToUpdate);

    void deactivateUser(User user);
}
