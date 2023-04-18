package com.luis.project.cl.users.infrastructure.config.beans;

import com.luis.project.cl.users.BaseTest;
import com.luis.project.cl.users.application.ports.UserRepositoryPort;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;


class AppConfigTest extends BaseTest {


    private static AppConfig appConfig;


    @Mock
    private UserRepositoryPort userPersistencePort;


    @BeforeAll
    static void beforeAll() {
        appConfig = new AppConfig();
    }

    @Test
    void validBeanConfigurationClass() {
        Assertions.assertThat(appConfig.userService(userPersistencePort)).isNotNull();
    }

}