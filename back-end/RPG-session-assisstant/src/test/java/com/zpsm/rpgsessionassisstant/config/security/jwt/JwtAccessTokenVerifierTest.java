package com.zpsm.rpgsessionassisstant.config.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtAccessTokenVerifierTest {

    @Mock
    private MockFilterChain mockFilterChain;
    @Mock
    private ObjectMapper mockObjectMapper;
    private MockHttpServletRequest mockHttpServletRequest;
    private MockHttpServletResponse mockHttpServletResponse;
    private JwtAccessTokenVerifier jwtAccessTokenVerifier;
    private final Clock clock = Clock.fixed(Instant.parse("2023-04-03T09:00:25Z"), ZoneId.systemDefault());

    @BeforeEach
    void setUp() {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.setSecret("secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret");
        jwtConfig.setIgnoredPaths(List.of("/login", "/api/token/refresh", "/h2-console"));
        mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletResponse = new MockHttpServletResponse();
        jwtAccessTokenVerifier = new JwtAccessTokenVerifier(
            jwtConfig,
            Algorithm.HMAC256(jwtConfig.getSecret()),
            clock,
            mockObjectMapper);
    }

    @Test
    void givenCorrectTokenShouldAuthenticate() throws ServletException, IOException {
        // given
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0b3d5IiwiZXhwIjoxNjgyNTg1MTI1LCJpYXQiOjE2ODA1MTE1MjUsImF1dGhvcml0aWVzIjpbIlJPTEVfUExBWUVSIl19.prLSpSP7gfX05L_P3oPbi9GbWwNWFDvSLEFAz7FjIDU");

        // when
        jwtAccessTokenVerifier.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        // then
        verify(mockFilterChain, only()).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    @Test
    void givenNullOrEmptyTokenShouldExitEarlier() throws ServletException, IOException {
        // given
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "");

        // when
        jwtAccessTokenVerifier.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        // then
        verify(mockFilterChain, only()).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/login", "/api/token/refresh"})
    void givenAppropriatePathShouldExitEarlier(String path) throws ServletException, IOException {
        // given
        mockHttpServletRequest.setServletPath(path);

        // when
        jwtAccessTokenVerifier.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        // then
        verify(mockFilterChain, only()).doFilter(mockHttpServletRequest, mockHttpServletResponse);
    }

    @Test
    void givenJWTVerificationExceptionShouldExitEarlier() throws ServletException, IOException {
        // given
        MockHttpServletResponse expected = new MockHttpServletResponse();
        expected.setStatus(HttpStatus.UNAUTHORIZED.value());
        mockHttpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "incorrect-token");

        // when
        jwtAccessTokenVerifier.doFilterInternal(mockHttpServletRequest, mockHttpServletResponse, mockFilterChain);

        // then
        verify(mockFilterChain, only()).doFilter(mockHttpServletRequest, mockHttpServletResponse);
        assertEquals(expected.getStatus(), mockHttpServletResponse.getStatus());
    }

}
