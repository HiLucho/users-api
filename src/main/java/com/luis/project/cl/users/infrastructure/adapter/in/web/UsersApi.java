package com.luis.project.cl.users.infrastructure.adapter.in.web;


import com.luis.project.cl.users.domain.User;
import com.luis.project.cl.users.domain.request.UserRequest;
import com.luis.project.cl.users.domain.response.UserCreated;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@SuppressWarnings("unused")
@RequestMapping("v1/user")
public interface UsersApi {

    @PostMapping(path = "create")
    ResponseEntity<UserCreated> createUser(@RequestBody @Validated UserRequest user);

    @GetMapping(path = "{id}")
    ResponseEntity<User> getUser(@PathVariable UUID id);

    @PutMapping(path = "update/{id}")
    ResponseEntity<UserCreated> updateUser(@PathVariable UUID id, @RequestBody @Validated UserRequest user);

    @PutMapping(path = "deactivate/{id}")
    ResponseEntity<?> deactivateUser(@PathVariable UUID id);
}
