package com.luis.project.cl.users.infrastructure.adapter.in.web.controller;

import com.luis.project.cl.users.application.services.UserService;
import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.request.UserRequest;
import com.luis.project.cl.users.domain.response.UserCreated;
import com.luis.project.cl.users.infrastructure.adapter.in.web.UsersApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Validated
@RequiredArgsConstructor
@RestController
public class UsersController implements UsersApi {

    private final UserService userService;

    @Override
    public ResponseEntity<UserCreated> createUser(UserRequest user) {
        var newUser = userService.createUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(newUser);
    }

    @Override
    public ResponseEntity<User> getUser(UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Override
    public ResponseEntity<UserCreated> updateUser(UUID id, UserRequest user) {
        return ResponseEntity.ok(userService.updateUser(user, id));
    }

    @Override
    public ResponseEntity<?> deactivateUser(UUID id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }


}
