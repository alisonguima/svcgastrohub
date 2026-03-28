package com.restaurant.gastrohub.application.port.output;

import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import com.restaurant.gastrohub.application.domain.user.User;

public interface UserPostgresPort {

  User saveUser(UserEntity userEntity);
  boolean existsByLogin(String login);
  boolean existsByEmail(String email);

}
