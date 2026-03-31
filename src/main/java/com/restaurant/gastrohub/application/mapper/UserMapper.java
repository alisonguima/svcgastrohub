package com.restaurant.gastrohub.application.mapper;

import com.restaurant.gastrohub.adapter.input.request.CreateUserRequest;
import com.restaurant.gastrohub.adapter.input.request.UpdateUserPasswordRequest;
import com.restaurant.gastrohub.adapter.input.request.UpdateUserRequest;
import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.adapter.input.response.GetUserResponse;
import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import com.restaurant.gastrohub.application.domain.user.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User userContractToDomain(CreateUserRequest createUserRequest);

  User userUpdateContractToDomain(UpdateUserRequest updateUserRequest);

  UserEntity domainUserToEntity(User user);

  @Mapping(target = "password", source = "newPassword")
  User updatePasswordUser(@MappingTarget User user, String newPassword);

  User userEntityToDomain(UserEntity userEntity);

  CreateUserResponse domainUserToContract(User user);

  GetUserResponse domainUserToGetUserResponse(User user);

  @Mapping(target = "password", source = "currentPassword")
  User updatePasswordRequestToDomain(UpdateUserPasswordRequest updateUserPasswordRequest);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  @Mapping(target = "id", source = "id")
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "lastUpdateAt", ignore = true)
  @Mapping(target = "newPassword", ignore = true)
  User mergeUserForUpdate(@MappingTarget User target, User source, Long id);
}
