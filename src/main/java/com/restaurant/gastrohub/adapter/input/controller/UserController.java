package com.restaurant.gastrohub.adapter.input.controller;

import com.restaurant.gastrohub.adapter.input.request.CreateUserRequest;
import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.application.mapper.UserMapper;
import com.restaurant.gastrohub.application.port.input.UserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/users/v1/users"})
@RequiredArgsConstructor
@Slf4j
public class UserController {

  private final UserUseCase userUseCase;

  @PostMapping
  public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest userRequest) {

    log.info("createUser - Receiving request to create user: name={}, email={}, login={}, userType={}",
        userRequest.name(), userRequest.email(), userRequest.login(), userRequest.userType());

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(userUseCase.createUser(
            UserMapper.INSTANCE.userContractToDomain(userRequest)));
  }
}
