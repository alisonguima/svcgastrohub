package com.restaurant.gastrohub.application.port.output;

import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import com.restaurant.gastrohub.application.domain.user.User;

import java.util.List;

public interface UserPostgresPort {

  User saveUser(UserEntity userEntity);
  void updateUser(UserEntity userEntity);
  boolean existsByLogin(String login);
  boolean existsByEmail(String email);
  User getUserById(Long id);
  User getUserByLogin(String login);
  List<User> getUserByName(String name);
  void deleteUser(Long id);

}
