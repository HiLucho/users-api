package com.luis.project.cl.users.application.services;


import com.luis.project.cl.users.domain.User;

import java.util.UUID;

public interface UserService {

    User createUser(User user);
    User getUser(UUID id, UUID token);
    User updateUser(User user, UUID token);
    void deleteUser(UUID id, UUID token);
    boolean existsUserById(UUID id);

}
