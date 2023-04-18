package com.luis.project.cl.users.infrastructure.adapter.out.client.interceptor;

import com.luis.project.cl.users.application.ports.ApiAuthClientPort;
import com.luis.project.cl.users.infrastructure.adapter.out.persistence.dto.AuthResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomRestTemplateInterceptorTest {

    private ClientHttpRequestInterceptor clientHttpRequestInterceptor;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private ApiAuthClientPort authClientPort;
    @Mock
    private ClientHttpResponse clientHttpResponse;
    @Mock
    private HttpRequest request;
    @Mock
    private ClientHttpRequestExecution execution;
    @Mock
    private Cache cache;

    @BeforeEach
    void setUp() {
        clientHttpRequestInterceptor = new CustomRestTemplateInterceptor(authClientPort, cacheManager, "ccc");
    }

    @Test
    void interceptWithoutHeaderAuthorization() throws IOException {
        // Preparing data
        AuthResponseDto authResponseDto = AuthResponseDto.builder().accessToken("token").build();
        HttpHeaders httpHeaders = new HttpHeaders();

        // Mocks & Stubs configuration
        when(request.getHeaders()).thenReturn(httpHeaders);
        when(authClientPort.getAuthorizationToken()).thenReturn(authResponseDto);
        when(execution.execute(any(), any())).thenReturn(clientHttpResponse);

        // Business logic execution
        ClientHttpResponse clientHttpResponse = clientHttpRequestInterceptor.intercept(request,
                "{}".getBytes(StandardCharsets.UTF_8), execution);

        // Validating mocks behaviour
        authClientPort.getAuthorizationToken();

        // Validating results
        assertThat(clientHttpResponse).isNotNull();
    }

    @Test
    void interceptWithHeaderAuthorization() throws IOException {
        // Preparing data
        AuthResponseDto authResponseDto = AuthResponseDto.builder()
                .accessToken("token")
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth("token");

        // Mocks & Stubs configuration
        when(request.getHeaders()).thenReturn(httpHeaders);
        when(execution.execute(any(), any())).thenReturn(clientHttpResponse);

        // Business logic execution
        ClientHttpResponse clientHttpResponse = clientHttpRequestInterceptor.intercept(request, "{}".getBytes(StandardCharsets.UTF_8), execution);

        // Validating mocks behaviour
        verify(authClientPort, never()).getAuthorizationToken();

        // Validating results
        assertThat(clientHttpResponse).isNotNull();
    }

    @Test
    void interceptWithRequestUnauthorized() throws IOException {
        // Preparing data
        AuthResponseDto authResponseDto = AuthResponseDto.builder()
                .accessToken("token")
                .build();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth("token");

        // Mocks & Stubs configuration
        when(request.getHeaders()).thenReturn(httpHeaders);
        when(authClientPort.getAuthorizationToken()).thenReturn(authResponseDto);
        when(clientHttpResponse.getStatusCode()).thenReturn(HttpStatus.UNAUTHORIZED);
        when(execution.execute(any(), any())).thenReturn(clientHttpResponse);
        when(cacheManager.getCache("ccc")).thenReturn(cache);

        // Business logic execution
        ClientHttpResponse clientHttpResponse = clientHttpRequestInterceptor.intercept(request, "{}".getBytes(StandardCharsets.UTF_8), execution);

        authClientPort.getAuthorizationToken();

        // Validating results
        assertThat(clientHttpResponse).isNotNull();
    }
}