package com.restaurant.gastrohub.adapter.input.controller;

import com.restaurant.gastrohub.adapter.input.request.CreateUserRequest;
import com.restaurant.gastrohub.adapter.input.request.UpdateUserPasswordRequest;
import com.restaurant.gastrohub.adapter.input.request.UpdateUserRequest;
import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.adapter.input.response.GetUserResponse;
import com.restaurant.gastrohub.application.mapper.UserMapper;
import com.restaurant.gastrohub.application.port.input.UserUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping(value = {"/api/v1/users"})
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "APIs for managing users in the GastroHub system")
public class UserController {

  private final UserUseCase userUseCase;

  @PostMapping
  @Operation(summary = "Create a new user", description = "Creates a new user with the provided details. The password field is required and will be encoded before storage.")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User creation request with required fields", content = @Content(schema = @Schema(implementation = CreateUserRequest.class)))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(schema = @Schema(implementation = CreateUserResponse.class))),
      @ApiResponse(responseCode = "400", description = "Invalid input data - missing or invalid required fields"),
      @ApiResponse(responseCode = "422", description = "Business rule violation - email or login already exists")
  })
  public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest userRequest) {

    log.info("createUser - Receiving request to create user: name={}, email={}, login={}, userType={}",
        userRequest.name(), userRequest.email(), userRequest.login(), userRequest.userType());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userUseCase.createUser(
            UserMapper.INSTANCE.userContractToDomain(userRequest)));
  }


  @GetMapping("/{id}")
  @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their unique ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User retrieved successfully", content = @Content(schema = @Schema(implementation = GetUserResponse.class))),
      @ApiResponse(responseCode = "422", description = "User not found with the specified ID")
  })
  public ResponseEntity<GetUserResponse> getUser(@PathVariable Long id) {
    log.info("getUser - Receiving request to get user: id={}", id);
    return ResponseEntity.ok(userUseCase.getUser(id));
  }

  @GetMapping(params = "name")
  @Operation(summary = "Get users by name", description = "Retrieves all users matching the provided name. Supports partial name matching (case-insensitive)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = @Content(schema = @Schema(implementation = GetUserResponse.class))),
      @ApiResponse(responseCode = "422", description = "No users found with the specified name")
  })
  public ResponseEntity<List<GetUserResponse>> getUserByName(@RequestParam String name) {
    log.info("getUserByName - Receiving request to get users by name: name={}", name);
    return ResponseEntity.ok(userUseCase.getUserByName(name));
  }

  @PatchMapping("/{id}")
  @Operation(summary = "Update user", description = "Updates an existing user's information. Only provided fields will be updated.")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User update request with optional fields", content = @Content(schema = @Schema(implementation = UpdateUserRequest.class)))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User updated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input data"),
      @ApiResponse(responseCode = "422", description = "User not found or business rule violation (duplicate email/login)")
  })
  public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest userRequest) {
    log.info("updateUser - Receiving request to update user: id={}, name={}, email={}, login={}, userType={}",
        id, userRequest.name(), userRequest.email(), userRequest.login(), userRequest.userType());

    userUseCase.updateUser(id,
        UserMapper.INSTANCE.userUpdateContractToDomain(userRequest));

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @PatchMapping("/{id}/password")
  @Operation(summary = "Update user password", description = "Updates the password for a specific user. The current password must be provided for validation.")
  @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Password update request with current and new password", content = @Content(schema = @Schema(implementation = UpdateUserPasswordRequest.class)))
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Password updated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input data"),
      @ApiResponse(responseCode = "422", description = "User not found or current password is incorrect")
  })
  public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UpdateUserPasswordRequest updatePasswordRequest) {
    log.info("updatePassword - Receiving request to update password for userId={}", id);

    userUseCase.updatePassword(id,
        UserMapper.INSTANCE.updatePasswordRequestToDomain(updatePasswordRequest));

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete user", description = "Deletes a user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User deleted successfully"),
      @ApiResponse(responseCode = "422", description = "User not found")
  })
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    log.info("deleteUser - Receiving request to delete user with userId={}", id);

    userUseCase.deleteUser(id);

    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }
}
