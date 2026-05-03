package com.restaurant.gastrohub.adapter.output.persistence.repository;

import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  boolean existsByLogin(String login);
  boolean existsByEmail(String email);
  Optional<UserEntity> findByLogin(String login);
  List<UserEntity> findByNameContainingIgnoreCase(String name);

}
