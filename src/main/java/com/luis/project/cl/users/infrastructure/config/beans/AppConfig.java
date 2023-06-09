package com.luis.project.cl.users.infrastructure.config.beans;


import com.luis.project.cl.users.application.ports.UserRepositoryPort;
import com.luis.project.cl.users.application.services.UserService;
import com.luis.project.cl.users.application.services.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("unused")
@Configuration
public class AppConfig {
    @Bean
    public UserService userService(UserRepositoryPort userPersistencePort) {
        return new UserServiceImpl(userPersistencePort);
    }

}
