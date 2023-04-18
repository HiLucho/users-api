package com.luis.project.cl.users.application.services;


import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.request.UserRequest;
import com.luis.project.cl.users.domain.response.UserCreated;

import java.util.UUID;

public interface UserService {

    UserCreated createUser(UserRequest userRequest);

    User getUser(UUID id);

    UserCreated updateUser(UserRequest userRequest, UUID id);

    void deactivateUser(UUID id);

}
