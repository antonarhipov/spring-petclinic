package org.springframework.samples.petclinic.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.samples.petclinic.rest.ApiError;
import org.springframework.samples.petclinic.rest.ApiResponse;

/**
 * This class is used to handle authentication exceptions.
 * It returns a standardized error response when authentication fails.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiError apiError = new ApiError(
            "UNAUTHORIZED",
            HttpStatus.UNAUTHORIZED.toString(),
            authException.getMessage()
        );

        ApiResponse<Void> errorResponse = ApiResponse.error("Authentication failed", apiError);

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
