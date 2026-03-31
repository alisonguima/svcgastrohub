package com.restaurant.gastrohub.adapter.input.controller;

import com.restaurant.gastrohub.adapter.input.request.CreateUserRequest;
import com.restaurant.gastrohub.adapter.input.request.UpdateUserPasswordRequest;
import com.restaurant.gastrohub.adapter.input.request.UpdateUserRequest;
import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.adapter.input.response.GetUserResponse;
import com.restaurant.gastrohub.application.mapper.UserMapper;
import com.restaurant.gastrohub.application.port.input.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/users/v1/users"})
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users in the GastroHub system")
public class UserController {

  private final UserUseCase userUseCase;

  @PostMapping
  @Operation(summary = "Create a new user", description = "Creates a new user with the provided details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User created successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input data"),
      @ApiResponse(responseCode = "422", description = "Business rule violation (e.g., email or login already exists)")
  })
  public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest userRequest) {

    log.info("createUser - Receiving request to create user: name={}, email={}, login={}, userType={}",
        userRequest.name(), userRequest.email(), userRequest.login(), userRequest.userType());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userUseCase.createUser(
            UserMapper.INSTANCE.userContractToDomain(userRequest)));
  }

  @GetMapping
  @Operation(summary = "Get all users", description = "Retrieves a list of all users, optionally filtered by user type")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
  })
  public ResponseEntity<List<GetUserResponse>> getAllUsers(@RequestParam(required = false) String userType) {
    log.info("getAllUsers - Receiving request to get all users with userType={}", userType);

    return ResponseEntity.ok(
        userUseCase.getUsers(userType));
  }

  @GetMapping("/{userId}")
  @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
      @ApiResponse(responseCode = "422", description = "User not found")
  })
  public ResponseEntity<GetUserResponse> getUsers(@PathVariable Long userId) {
    log.info("getUsers- Receiving request to get user: id={}", userId);
    return ResponseEntity.ok(userUseCase.getUser(userId));
  }

  @PatchMapping("/{userId}")
  @Operation(summary = "Update user", description = "Updates an existing user's details")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User updated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input data"),
      @ApiResponse(responseCode = "422", description = "User not found or business rule violation")
  })
  public ResponseEntity<Void> updateUser(@PathVariable Long userId, @Valid @RequestBody UpdateUserRequest userRequest) {
    log.info("updateUser - Receiving request to update user: id={}, name={}, email={}, login={}, userType={}",
        userId, userRequest.name(), userRequest.email(), userRequest.login(), userRequest.userType());

    userUseCase.updateUser(userId,
        UserMapper.INSTANCE.userUpdateContractToDomain(userRequest));

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @PatchMapping("/{userId}/password")
  @Operation(summary = "Update user password", description = "Updates the password for a specific user")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Password updated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input data"),
      @ApiResponse(responseCode = "422", description = "User not found or password validation failed")
  })
  public ResponseEntity<Void> updatePassword(@PathVariable Long userId, @Valid @RequestBody UpdateUserPasswordRequest updatePasswordRequest) {
    log.info("updatePassword - Receiving request to update password for userId={}", userId);

    userUseCase.updatePassword(userId,
        UserMapper.INSTANCE.updatePasswordRequestToDomain(updatePasswordRequest));

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @DeleteMapping("/{userId}")
  @Operation(summary = "Delete user", description = "Deletes a user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User deleted successfully"),
      @ApiResponse(responseCode = "422", description = "User not found")
  })
  public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
    log.info("deleteUser - Receiving request to delete user with userId={}", userId);

    userUseCase.deleteUser(userId);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
