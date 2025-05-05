package org.springframework.samples.petclinic.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

/**
 * REST controller for managing {@link User}s.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "API for managing users")
public class UserRestController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRestController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get all users with pagination.
     * @param page the page number
     * @param size the page size
     * @return the list of users
     */
    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a paginated list of all users in the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<Page<User>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);

        return ResponseEntity.ok(ApiResponse.success("All users retrieved successfully", users));
    }

    /**
     * Get a user by id.
     * @param id the id of the user to retrieve
     * @return the user
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a user as per the id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<User>> getUser(@PathVariable("id") int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            ApiError error = new ApiError("USER_NOT_FOUND", HttpStatus.NOT_FOUND.toString(),
                                         "User not found with id: " + id);
            return new ResponseEntity<>(ApiResponse.error("User not found", error), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user.get()));
    }

    /**
     * Create a new user.
     * @param user the user to create
     * @return the created user
     */
    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or username/email already exists",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<User>> createUser(@Valid @RequestBody User user) {
        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            ApiError error = new ApiError("USERNAME_EXISTS", HttpStatus.BAD_REQUEST.toString(),
                                         "Username already exists: " + user.getUsername());
            return new ResponseEntity<>(ApiResponse.error("Username already exists", error), HttpStatus.BAD_REQUEST);
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            ApiError error = new ApiError("EMAIL_EXISTS", HttpStatus.BAD_REQUEST.toString(),
                                         "Email already exists: " + user.getEmail());
            return new ResponseEntity<>(ApiResponse.error("Email already exists", error), HttpStatus.BAD_REQUEST);
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>(ApiResponse.success("User created successfully", savedUser), HttpStatus.CREATED);
    }

    /**
     * Update an existing user.
     * @param id the id of the user to update
     * @param userDetails the user details to update
     * @return the updated user
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a user", description = "Updates an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or username/email already exists",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<User>> updateUser(
            @PathVariable("id") int id,
            @Valid @RequestBody User userDetails) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            ApiError error = new ApiError("USER_NOT_FOUND", HttpStatus.NOT_FOUND.toString(),
                                         "User not found with id: " + id);
            return new ResponseEntity<>(ApiResponse.error("User not found", error), HttpStatus.NOT_FOUND);
        }

        User existingUser = optionalUser.get();

        // Check if username is being changed and if it already exists
        if (!existingUser.getUsername().equals(userDetails.getUsername()) &&
            userRepository.existsByUsername(userDetails.getUsername())) {
            ApiError error = new ApiError("USERNAME_EXISTS", HttpStatus.BAD_REQUEST.toString(),
                                         "Username already exists: " + userDetails.getUsername());
            return new ResponseEntity<>(ApiResponse.error("Username already exists", error), HttpStatus.BAD_REQUEST);
        }

        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(userDetails.getEmail()) &&
            userRepository.existsByEmail(userDetails.getEmail())) {
            ApiError error = new ApiError("EMAIL_EXISTS", HttpStatus.BAD_REQUEST.toString(),
                                         "Email already exists: " + userDetails.getEmail());
            return new ResponseEntity<>(ApiResponse.error("Email already exists", error), HttpStatus.BAD_REQUEST);
        }

        // Update user details
        existingUser.setFirstName(userDetails.getFirstName());
        existingUser.setLastName(userDetails.getLastName());
        existingUser.setUsername(userDetails.getUsername());
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setRole(userDetails.getRole());
        existingUser.setEnabled(userDetails.isEnabled());

        // Only update password if it's provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);

        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updatedUser));
    }

    /**
     * Delete a user.
     * @param id the id of the user to delete
     * @return no content
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Deletes an existing user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted"),
        @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<Void> deleteUser(@PathVariable("id") int id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    /**
     * Get a user by username.
     * @param username the username of the user to retrieve
     * @return the user
     */
    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Returns a user as per the username")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<ApiResponse<User>> getUserByUsername(@PathVariable("username") String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            ApiError error = new ApiError("USER_NOT_FOUND", HttpStatus.NOT_FOUND.toString(),
                                         "User not found with username: " + username);
            return new ResponseEntity<>(ApiResponse.error("User not found", error), HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user.get()));
    }
}
