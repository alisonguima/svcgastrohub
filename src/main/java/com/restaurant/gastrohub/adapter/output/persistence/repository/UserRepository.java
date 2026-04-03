package com.restaurant.gastrohub.adapter.output.persistence.repository;

import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByLogin(String login);
  boolean existsByEmail(String email);
  List<UserEntity> findByNameContainingIgnoreCase(String name);

}
