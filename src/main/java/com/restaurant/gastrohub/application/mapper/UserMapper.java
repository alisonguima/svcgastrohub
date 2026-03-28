package com.restaurant.gastrohub.application.mapper;

import com.restaurant.gastrohub.adapter.input.request.CreateUserRequest;
import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import com.restaurant.gastrohub.application.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User userContractToDomain(CreateUserRequest createUserRequest);

  UserEntity domainUserToEntity(User user);

  User userEntityToDomain(UserEntity userEntity);

  CreateUserResponse domainUserToContract(User user);
}
