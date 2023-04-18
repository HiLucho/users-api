package com.luis.project.cl.users.infrastructure.config.beans;

import com.luis.project.cl.users.BaseTest;
import com.luis.project.cl.users.application.ports.ApiRpcClientPort;
import com.luis.project.cl.users.application.ports.PublishPort;
import com.luis.project.cl.users.infrastructure.adapter.out.client.pom.ApiPomAuthClient;
import com.luis.project.cl.users.infrastructure.config.apisettings.AppPubSubErrorSettings;
import com.luis.project.cl.users.infrastructure.config.apisettings.AppPubSubSomeProjectSettings;
import com.luis.project.cl.users.infrastructure.config.apisettings.AppSettings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class AppConfigTest extends BaseTest {

    private static AppConfig appConfig;

    private static AppPubSubErrorSettings appErrorSettings;
    private static AppSettings appSettings;
    private static AppPubSubSomeProjectSettings appPubSubSettings;


    @Mock
    private CacheManager cacheManager;
    @Mock
    private Resilience4JCircuitBreakerFactory resilience4JCircuitBreakerFactory;
    @Mock
    private ApiRpcClientPort clientPort;
    @Mock
    private PublishPort publishPort;
    @Mock
    private ApiPomAuthClient authClient;


    @BeforeAll
    static void beforeAll() {
        appErrorSettings = new AppPubSubErrorSettings();
        appPubSubSettings = new AppPubSubSomeProjectSettings();
        appSettings = new AppSettings();
        appErrorSettings.setProjectId("project-error");
        appErrorSettings.setTopicId("topic-error");
        appPubSubSettings.setProjectId("project");
        appPubSubSettings.setTopicId("topic-project");
        appPubSubSettings.setSubscriptionId("some-subscription");
        appSettings.setClientId("123");
        appSettings.setHost("htpps://www.some.com");
        appSettings.setCountry("CL");
        appSettings.setCommerce("IKS");
        appSettings.setClientSecret("SECRET");
        appSettings.setPath("path");
        appSettings.setPathAuth("path-auth");
        appSettings.setEnvironment("QA");
        appConfig = new AppConfig();
    }

    @Test
    void validBeanConfigurationClass() throws IOException {
        assertThat(appConfig.authTokenClientPomPort(resilience4JCircuitBreakerFactory, appSettings)).isNotNull();
        assertThat(appConfig.consumerPubSub(appPubSubSettings)).isNotNull();
        assertThat(appConfig.globalCustomConfiguration()).isNotNull();
        assertThat(appConfig.httpClient(resilience4JCircuitBreakerFactory, appSettings,
                authClient, cacheManager)).isNotNull();
        assertThat(appConfig.templateService(clientPort, publishPort)).isNotNull();
        assertThat(appConfig.publishPort(appPubSubSettings)).isNotNull();
        assertThat(appConfig.errorPublisherPort(appErrorSettings)).isNotNull();
    }

}