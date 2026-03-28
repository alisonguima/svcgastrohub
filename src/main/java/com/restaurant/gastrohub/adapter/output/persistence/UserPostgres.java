package com.restaurant.gastrohub.adapter.output.persistence;

import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import com.restaurant.gastrohub.adapter.output.persistence.repository.UserRepository;
import com.restaurant.gastrohub.application.domain.user.User;
import com.restaurant.gastrohub.application.mapper.UserMapper;
import com.restaurant.gastrohub.application.port.output.UserPostgresPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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
  public boolean existsByLogin(String login) {
    return userRepository.existsByLogin(login);
  }

  @Override
  public boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
