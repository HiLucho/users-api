package com.luis.project.cl.users.infrastructure.adapter.out.client.pom;

import com.luis.project.cl.users.domain.exception.ApiAuthenticationException;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.AuthResponseDto;
import com.luis.project.cl.users.infrastructure.config.apisettings.AppSettings;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
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

import java.net.URI;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@ExtendWith(MockitoExtension.class)
class AuthClientTest {

    @Autowired
    private MockRestServiceServer restServiceServer;

    @Mock
    private AppSettings settings;
    private ApiPomAuthClient authClient;
    private URI uriAuth;

    @BeforeEach
    void setUp() {
        RestTemplate restTemplate = new RestTemplate();
        Resilience4JCircuitBreakerFactory circuitBreakerFactory = new Resilience4JCircuitBreakerFactory(
                CircuitBreakerRegistry.ofDefaults(), TimeLimiterRegistry.ofDefaults(), null);
        restServiceServer = MockRestServiceServer.createServer(restTemplate);
        authClient = new ApiPomAuthClient(circuitBreakerFactory, restTemplate, settings);
        when(settings.getHost()).thenReturn("http://www.some.com");
        when(settings.getPathAuth()).thenReturn("oauth/token");
        uriAuth = UriComponentsBuilder.fromUriString(settings.getHost())
                .pathSegment(settings.getPathAuth())
                .build()
                .toUri();
    }

    @Test
    void given_validRequest_then_return_token() {
        // Preparing data
        String response = "{\"access_token\":\"eyJhbGciOiJSUzI1Ni\",\"expires_in\":2592000,\"token_type\":\"Bearer\"}";

        // Mocks & Stubs configuration
        restServiceServer.expect(requestTo(uriAuth))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK).body(response)
                        .contentType(MediaType.APPLICATION_JSON));

        // Business logic execution
        AuthResponseDto authResponseDto = authClient.getAuthorizationToken();

        // Validating results
        assertThat(authResponseDto).isNotNull()
                .hasFieldOrPropertyWithValue("tokenType", "Bearer")
                .hasFieldOrPropertyWithValue("accessToken", "eyJhbGciOiJSUzI1Ni")
                .hasFieldOrPropertyWithValue("expiresIn", 2592000L);
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 401, 404, 500, 503})
    void gettingRequest_thenReturnError(int statusCode) {
        // Mocks & Stubs configuration
        restServiceServer.expect(requestTo(uriAuth))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.valueOf(statusCode)));

        // Business logic execution
        assertThrows(ApiAuthenticationException.class, () -> authClient.getAuthorizationToken());
    }
}