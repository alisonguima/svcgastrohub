package com.restaurant.gastrohub.application.service;

import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.user.User;
import com.restaurant.gastrohub.application.exception.DefaultException;
import com.restaurant.gastrohub.application.mapper.UserMapper;
import com.restaurant.gastrohub.application.port.input.UserUseCase;
import com.restaurant.gastrohub.application.port.output.UserPostgresPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserUseCase {

  private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
  private final UserPostgresPort userPostgresPort;

  @Override
  public CreateUserResponse createUser(User user) {

    throwIfExists(userPostgresPort.existsByEmail(user.getEmail()),
        "createUser - Email already in use: email={}", user.getEmail(),
        ApiConstants.EMAIL_ALREADY_EXISTS);

    throwIfExists(userPostgresPort.existsByLogin(user.getLogin()),
        "createUser - Login already in use: login={}", user.getLogin(),
        ApiConstants.LOGIN_ALREADY_EXISTS);

    return UserMapper.INSTANCE.domainUserToContract(
        userPostgresPort.saveUser(UserMapper.INSTANCE.domainUserToEntity(user)));
  }

  @Override
  public void updatePassword(Long userId, String currentPassword, String newPassword) {
    // TODO: buscar userEntity do repositorio pelo userId
    // UserEntity userEntity = userRepository.findById(userId).orElseThrow();

    // Verifica se a senha atual bate com o hash armazenado
    if (!passwordEncoder.matches(currentPassword, /* userEntity.getPassword() */ "")) {
      throw new IllegalArgumentException("Current password does not match");
    }

    // Só encoda e salva se a nova senha for diferente da atual
    if (!passwordEncoder.matches(newPassword, /* userEntity.getPassword() */ "")) {
      // userEntity.setPassword(passwordEncoder.encode(newPassword));
      // userRepository.save(userEntity);
      log.info("updatePassword - Password updated successfully for userId={}", userId);
    }
  }

  private void throwIfExists(boolean exists, String logMessage, Object logArg, String errorMessage) {
    Optional.of(exists)
        .filter(Boolean::booleanValue)
        .ifPresent(e -> {
          log.warn(logMessage, logArg);
          throw new DefaultException(errorMessage);
        });
  }
}
