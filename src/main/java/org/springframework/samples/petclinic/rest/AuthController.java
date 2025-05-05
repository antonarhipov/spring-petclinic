package org.springframework.samples.petclinic.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.samples.petclinic.security.JwtTokenProvider;
import org.springframework.samples.petclinic.security.CustomUserDetailsService;

/**
 * REST controller for authentication.
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API for authentication")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
            CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Login endpoint.
     * @param loginRequest the login request
     * @return the JWT token
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate a user and return a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful authentication",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUsername());

            String token = jwtTokenProvider.generateToken(userDetails);
            String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails);

            AuthResponse authResponse = new AuthResponse(token, refreshToken);

            return ResponseEntity.ok(ApiResponse.success("Authentication successful", authResponse));
        } catch (Exception e) {
            ApiError error = new ApiError("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED.toString(),
                                         "Invalid username or password");
            return new ResponseEntity<>(ApiResponse.error("Authentication failed", error), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Refresh token endpoint.
     * @param refreshTokenRequest the refresh token request
     * @return the new JWT token
     */
    @PostMapping("/refresh-token")
    @Operation(summary = "Refresh token", description = "Refresh a JWT token")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token refreshed",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid refresh token",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            String refreshToken = refreshTokenRequest.getRefreshToken();
            String username = jwtTokenProvider.extractUsername(refreshToken);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenProvider.isTokenValid(refreshToken, userDetails)) {
                String token = jwtTokenProvider.generateToken(userDetails);

                AuthResponse authResponse = new AuthResponse(token, refreshToken);

                return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", authResponse));
            } else {
                ApiError error = new ApiError("INVALID_REFRESH_TOKEN", HttpStatus.UNAUTHORIZED.toString(),
                                             "Invalid refresh token");
                return new ResponseEntity<>(ApiResponse.error("Token refresh failed", error), HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            ApiError error = new ApiError("INVALID_REFRESH_TOKEN", HttpStatus.UNAUTHORIZED.toString(),
                                         "Invalid refresh token");
            return new ResponseEntity<>(ApiResponse.error("Token refresh failed", error), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Login request.
     */
    public static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest() {
        }

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    /**
     * Refresh token request.
     */
    public static class RefreshTokenRequest {
        private String refreshToken;

        public RefreshTokenRequest() {
        }

        public RefreshTokenRequest(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    /**
     * Authentication response.
     */
    public static class AuthResponse {
        private String token;
        private String refreshToken;

        public AuthResponse() {
        }

        public AuthResponse(String token, String refreshToken) {
            this.token = token;
            this.refreshToken = refreshToken;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
