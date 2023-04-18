package com.luis.project.cl.users.application.ports;

import com.luis.project.cl.users.domain.User;

import java.util.Optional;
import java.util.UUID;

/**
 * User Persistence.
 */
public interface PhonePersistencePort {

  Optional<User> findById(UUID id);

  Optional<User> save(User user);

  Optional<User> update(User user);
}
