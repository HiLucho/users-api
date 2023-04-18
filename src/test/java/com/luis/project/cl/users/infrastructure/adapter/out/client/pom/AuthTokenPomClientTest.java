package com.luis.project.cl.users.infrastructure.adapter.out.client.pom;

import com.luis.project.cl.users.JsonUtils;
import com.luis.project.cl.users.application.ports.ApiAuthClientPort;
import com.luis.project.cl.users.domain.exception.ApiAuthenticationException;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.AuthResponseDto;
import com.luis.project.cl.users.infrastructure.config.apisettings.AppSettings;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(MockitoExtension.class)
class AuthTokenPomClientTest {

    @Autowired
    private MockRestServiceServer restServiceServer;
    @Mock
    private AppSettings appSettings;
    private ApiAuthClientPort apiAuthClientPort;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        Resilience4JCircuitBreakerFactory circuitBreakerFactory = new Resilience4JCircuitBreakerFactory(
                CircuitBreakerRegistry.ofDefaults(),
                TimeLimiterRegistry.ofDefaults(),
                null);
        restServiceServer = MockRestServiceServer.createServer(restTemplate);
        apiAuthClientPort = new ApiPomAuthClient(circuitBreakerFactory, restTemplate, appSettings);
    }

    @Test
    void getAuthorizationTokenSuccessfully() throws IOException {
        // Preparing data
        String responsePayload = JsonUtils.loadJsonPayload("static/AuthResponse.json");

        // Mocks & Stubs configuration
        when(appSettings.getHost()).thenReturn("http://localhost");
        when(appSettings.getPathAuth()).thenReturn("authorization");
        var uri = UriComponentsBuilder.fromUriString(appSettings.getHost())
                .pathSegment(appSettings.getPathAuth())
                .build()
                .toUri();
        restServiceServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                        .body(responsePayload)
                        .contentType(MediaType.APPLICATION_JSON));

        // Business logic execution
        AuthResponseDto authorizationToken = apiAuthClientPort.getAuthorizationToken();

        // Validating results
        AssertionsForClassTypes.assertThat(authorizationToken).isNotNull()
                .hasFieldOrPropertyWithValue("tokenType", "BearerToken")
                .hasFieldOrPropertyWithValue("accessToken", "vpeXXLQ7QCtSkim8YSZdN251BMZU")
                .hasFieldOrPropertyWithValue("expiresIn", 3599L);
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 401, 409, 500, 503})
    void getAuthorizationTokenFailed(int statusCode) {
        // Mocks & Stubs configuration
        when(appSettings.getHost()).thenReturn("http://localhost");
        when(appSettings.getPathAuth()).thenReturn("authorization");
        var uri = UriComponentsBuilder.fromUriString(appSettings.getHost())
                .pathSegment(appSettings.getPathAuth())
                .build()
                .toUri();
        restServiceServer
                .expect(requestTo(uri))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.valueOf(statusCode)));

        // Business logic execution
        assertThrows(ApiAuthenticationException.class, () -> apiAuthClientPort.getAuthorizationToken());
    }
}