package com.restaurant.gastrohub.adapter.output.persistence;

import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import com.restaurant.gastrohub.adapter.output.persistence.repository.UserRepository;
import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.user.User;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import com.restaurant.gastrohub.application.exception.DefaultException;
import com.restaurant.gastrohub.application.mapper.UserMapper;
import com.restaurant.gastrohub.application.port.output.UserPostgresPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserPostgres implements UserPostgresPort {

  private final UserRepository userRepository;

  @Override
  public User saveUser(UserEntity userEntity) {
    return UserMapper.INSTANCE.userEntityToDomain(userRepository.save(userEntity));
  }

  @Override
  public void updateUser(UserEntity userEntity) {
    userRepository.findById(userEntity.getId())
        .ifPresentOrElse(
            existingUser -> userRepository.save(userEntity), () -> {
              throw new DefaultException(ApiConstants.USER_NOT_FOUND + userEntity.getId());
            });
  }

  @Override
  public boolean existsByLogin(String login) {
    return userRepository.existsByLogin(login);
  }

  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Override
  public User getUserById(Long id) {
    return UserMapper.INSTANCE.userEntityToDomain(
        userRepository.findById(id)
            .orElseThrow(() -> new DefaultException(ApiConstants.USER_NOT_FOUND + id)));
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.findById(id)
        .ifPresentOrElse(
            user -> userRepository.deleteById(id), () -> {
              throw new DefaultException(ApiConstants.USER_NOT_FOUND + id);
            });
  }

  @Override
  public List<User> getUsersByUserType(UserType userType) {
    return userRepository.findByUserType(userType)
        .stream()
        .map(UserMapper.INSTANCE::userEntityToDomain)
        .toList();
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll()
        .stream()
        .map(UserMapper.INSTANCE::userEntityToDomain)
        .toList();
  }
}
