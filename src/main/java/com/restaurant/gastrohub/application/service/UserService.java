package com.restaurant.gastrohub.application.service;

import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.adapter.input.response.GetUserResponse;
import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.user.User;
import com.restaurant.gastrohub.application.exception.DefaultException;
import com.restaurant.gastrohub.application.mapper.UserMapper;
import com.restaurant.gastrohub.application.port.input.UserUseCase;
import com.restaurant.gastrohub.application.port.output.UserPostgresPort;
import com.restaurant.gastrohub.application.util.ConflictValidatorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserUseCase {

  private final PasswordEncoder passwordEncoder;
  private final UserPostgresPort userPostgresPort;

  @Override
  public CreateUserResponse createUser(User user) {

    log.info("createUser - Receiving request to create user: email={}, login={}, userType={}",
        user.getEmail(), user.getLogin(), user.getUserType());

    ConflictValidatorUtils.throwIfExists(userPostgresPort.existsByEmail(user.getEmail()),
        "createUser - Email already in use: email={}", user.getEmail(),
        ApiConstants.EMAIL_ALREADY_EXISTS);

    ConflictValidatorUtils.throwIfExists(userPostgresPort.existsByLogin(user.getLogin()),
        "createUser - Login already in use: login={}", user.getLogin(),
        ApiConstants.LOGIN_ALREADY_EXISTS);

    CreateUserResponse response = UserMapper.INSTANCE.domainUserToContract(
        userPostgresPort.saveUser(UserMapper.INSTANCE.domainUserToEntity(user)));

    log.info("createUser - User created successfully: id={}, email={}, login={}",
        response.id(), response.email(), response.login());
    return response;
  }

  @Override
  public void updateUser(Long userId, User user) {
    log.info("updateUser - Updating user with userId={}", userId);
    
    User existingUser = userPostgresPort.getUserById(userId);

    Optional.ofNullable(user.getEmail())
        .ifPresent(email -> ConflictValidatorUtils.throwIfExists(
            userPostgresPort.existsByEmail(email) && !email.equals(existingUser.getEmail()),
            "updateUser - Email already in use: email={}", email,
            ApiConstants.EMAIL_ALREADY_EXISTS));

    Optional.ofNullable(user.getLogin())
        .ifPresent(login -> ConflictValidatorUtils.throwIfExists(
            userPostgresPort.existsByLogin(login) && !login.equals(existingUser.getLogin()),
            "updateUser - Login already in use: login={}", login,
            ApiConstants.LOGIN_ALREADY_EXISTS));

    userPostgresPort.updateUser(
        UserMapper.INSTANCE.domainUserToEntity(
            UserMapper.INSTANCE.mergeUserForUpdate(existingUser, user, userId)));

    log.info("updateUser - User updated successfully: userId={}", userId);
  }

  @Override
  public void updatePassword(Long userId, User user) {
    log.info("updatePassword - Updating password for userId={}", userId);
    User existingUser = userPostgresPort.getUserById(userId);

    Optional.of(passwordEncoder.matches(user.getPassword(), existingUser.getPassword()))
        .filter(Boolean::booleanValue)
        .orElseThrow(() -> new DefaultException(ApiConstants.INVALID_PASSWORD));

    Optional.of(passwordEncoder.matches(user.getNewPassword(), existingUser.getPassword()))
        .filter(match -> !match)
        .ifPresent(match -> {
          userPostgresPort.updateUser(
              UserMapper.INSTANCE.domainUserToEntity(
                  UserMapper.INSTANCE.updatePasswordUser(
                      existingUser, passwordEncoder.encode(user.getNewPassword()))));
          log.info("updatePassword - Password updated successfully for userId={}", userId);
        });
  }

  @Override
  public GetUserResponse getUser(Long userId) {
    log.info("getUser - Fetching user with userId={}", userId);
    return UserMapper.INSTANCE.domainUserToGetUserResponse(userPostgresPort.getUserById(userId));
  }

  @Override
  public List<GetUserResponse> getUserByName(String name) {
    log.info("getUserByName - Fetching users with name={}", name);
    return userPostgresPort.getUserByName(name)
        .stream()
        .map(UserMapper.INSTANCE::domainUserToGetUserResponse)
        .toList();
  }

  @Override
  public void deleteUser(Long userId) {
    log.info("deleteUser - Deleting user with userId={}", userId);
    userPostgresPort.deleteUser(userId);
    log.info("deleteUser - User deleted successfully: userId={}", userId);
  }
}
