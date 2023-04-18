package com.luis.project.cl.users;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UsersApplicationTest extends BaseTest {
    @InjectMocks
    UsersApplication mock;

    @Test
    void contextLoads() {
        UsersApplication usersApplication = new UsersApplication();
        Assertions.assertNotNull(usersApplication);
    }
}