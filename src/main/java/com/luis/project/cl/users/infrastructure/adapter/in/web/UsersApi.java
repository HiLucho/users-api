package com.luis.project.cl.users.infrastructure.adapter.in.web;


import com.luis.project.cl.users.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("unused")
@RequestMapping("v1/user")
public interface UsersApi {

    @PostMapping(path = "create")
    ResponseEntity<User> createUser(@RequestBody @Validated User user);

}
