package com.restaurant.gastrohub.application.service;

import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.adapter.input.response.GetUserResponse;
import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import com.restaurant.gastrohub.application.domain.user.User;
import com.restaurant.gastrohub.application.exception.DefaultException;
import com.restaurant.gastrohub.application.port.output.UserPostgresPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

  @Mock
  private UserPostgresPort userPostgresPort;

  @InjectMocks
  private UserService userService;

  private BCryptPasswordEncoder realEncoder = new BCryptPasswordEncoder();

  private User user;
  private UserEntity userEntity;
  private CreateUserResponse createUserResponse;
  private GetUserResponse getUserResponse;
  private ZonedDateTime testDateTime;

  @BeforeEach
  void setUp() {
    testDateTime = ZonedDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"));

    user = new User();
    user.setId(1L);
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setLogin("johndoe");
    user.setPassword("password");
    user.setNewPassword("newpassword");
    user.setUserType(UserType.OWNER);
    user.setAddress("123 Main St");
    user.setLastUpdateAt(testDateTime);

    userEntity = new UserEntity();
    userEntity.setId(1L);
    userEntity.setName("John Doe");
    userEntity.setEmail("john@example.com");
    userEntity.setLogin("johndoe");
    userEntity.setPassword("password");
    userEntity.setUserType(UserType.OWNER);
    userEntity.setAddress("123 Main St");

    createUserResponse = new CreateUserResponse("1", "John Doe", "john@example.com", "johndoe");

    getUserResponse = new GetUserResponse("1", "John Doe", "john@example.com", "johndoe",
        UserType.OWNER, testDateTime, "123 Main St");
  }

  @Test
  @DisplayName("Should create user successfully")
  void testCreateUserSuccess() {
    when(userPostgresPort.existsByEmail(user.getEmail())).thenReturn(false);
    when(userPostgresPort.existsByLogin(user.getLogin())).thenReturn(false);
    when(userPostgresPort.saveUser(any(UserEntity.class))).thenReturn(user);

    CreateUserResponse response = userService.createUser(user);

    assertThat(response).isEqualTo(createUserResponse);
    verify(userPostgresPort).existsByEmail(user.getEmail());
    verify(userPostgresPort).existsByLogin(user.getLogin());
    verify(userPostgresPort).saveUser(any(UserEntity.class));
  }

  @Test
  @DisplayName("Should throw exception when email already exists")
  void testCreateUserEmailConflict() {
    when(userPostgresPort.existsByEmail(user.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> userService.createUser(user))
        .isInstanceOf(DefaultException.class)
        .hasMessage(ApiConstants.EMAIL_ALREADY_EXISTS);

    verify(userPostgresPort).existsByEmail(user.getEmail());
    verify(userPostgresPort, never()).existsByLogin(anyString());
  }

  @Test
  @DisplayName("Should throw exception when login already exists")
  void testCreateUserLoginConflict() {
    when(userPostgresPort.existsByEmail(user.getEmail())).thenReturn(false);
    when(userPostgresPort.existsByLogin(user.getLogin())).thenReturn(true);

    assertThatThrownBy(() -> userService.createUser(user))
        .isInstanceOf(DefaultException.class)
        .hasMessage(ApiConstants.LOGIN_ALREADY_EXISTS);

    verify(userPostgresPort).existsByEmail(user.getEmail());
    verify(userPostgresPort).existsByLogin(user.getLogin());
  }

  @Test
  @DisplayName("Should update user successfully")
  void testUpdateUserSuccess() {
    User updateUser = new User();
    updateUser.setEmail("new@example.com");
    updateUser.setLogin("newlogin");

    User existingUser = new User();
    existingUser.setId(1L);
    existingUser.setName("John Doe");
    existingUser.setEmail("old@example.com");
    existingUser.setLogin("oldlogin");
    existingUser.setPassword("password");
    existingUser.setUserType(UserType.OWNER);
    existingUser.setAddress("123 Main St");

    when(userPostgresPort.getUserById(1L)).thenReturn(existingUser);
    when(userPostgresPort.existsByEmail(updateUser.getEmail())).thenReturn(false);
    when(userPostgresPort.existsByLogin(updateUser.getLogin())).thenReturn(false);

    userService.updateUser(1L, updateUser);

    verify(userPostgresPort).getUserById(1L);
    verify(userPostgresPort).existsByEmail(updateUser.getEmail());
    verify(userPostgresPort).existsByLogin(updateUser.getLogin());
    verify(userPostgresPort).updateUser(any(UserEntity.class));
  }

  @Test
  @DisplayName("Should throw exception when updating to existing email")
  void testUpdateUserEmailConflict() {
    User updateUser = new User();
    updateUser.setEmail("existing@example.com");

    User existingUser = new User();
    existingUser.setEmail("old@example.com");

    when(userPostgresPort.getUserById(1L)).thenReturn(existingUser);
    when(userPostgresPort.existsByEmail(updateUser.getEmail())).thenReturn(true);

    assertThatThrownBy(() -> userService.updateUser(1L, updateUser))
        .isInstanceOf(DefaultException.class)
        .hasMessage(ApiConstants.EMAIL_ALREADY_EXISTS);

    verify(userPostgresPort).getUserById(1L);
    verify(userPostgresPort).existsByEmail(updateUser.getEmail());
    verify(userPostgresPort, never()).existsByLogin(anyString());
  }

  @Test
  @DisplayName("Should throw exception when updating to existing login")
  void testUpdateUserLoginConflict() {
    User updateUser = new User();
    updateUser.setLogin("existinglogin");

    User existingUser = new User();
    existingUser.setLogin("oldlogin");

    when(userPostgresPort.getUserById(1L)).thenReturn(existingUser);
    when(userPostgresPort.existsByLogin(updateUser.getLogin())).thenReturn(true);

    assertThatThrownBy(() -> userService.updateUser(1L, updateUser))
        .isInstanceOf(DefaultException.class)
        .hasMessage(ApiConstants.LOGIN_ALREADY_EXISTS);

    verify(userPostgresPort).getUserById(1L);
    verify(userPostgresPort, never()).existsByEmail(anyString());
    verify(userPostgresPort).existsByLogin(updateUser.getLogin());
  }

  @Test
  @DisplayName("Should update password successfully")
  void testUpdatePasswordSuccess() {
    User existingUser = new User();
    existingUser.setPassword(realEncoder.encode("oldPassword"));

    User updateUser = new User();
    updateUser.setPassword("oldPassword");
    updateUser.setNewPassword("newPassword");

    when(userPostgresPort.getUserById(1L)).thenReturn(existingUser);

    userService.updatePassword(1L, updateUser);

    verify(userPostgresPort).getUserById(1L);
    verify(userPostgresPort).updateUser(any(UserEntity.class));
  }

  @Test
  @DisplayName("Should throw exception for invalid current password")
  void testUpdatePasswordInvalidCurrent() {
    User existingUser = new User();
    existingUser.setPassword(realEncoder.encode("different"));

    User updateUser = new User();
    updateUser.setPassword("wrongPassword");
    updateUser.setNewPassword("newPassword");

    when(userPostgresPort.getUserById(1L)).thenReturn(existingUser);

    assertThatThrownBy(() -> userService.updatePassword(1L, updateUser))
        .isInstanceOf(DefaultException.class)
        .hasMessage(ApiConstants.INVALID_PASSWORD);

    verify(userPostgresPort).getUserById(1L);
    verify(userPostgresPort, never()).updateUser(any());
  }

  @Test
  @DisplayName("Should not update when new password is same as current")
  void testUpdatePasswordSameAsCurrent() {
    User existingUser = new User();
    existingUser.setPassword(realEncoder.encode("oldPassword"));

    User updateUser = new User();
    updateUser.setPassword("oldPassword");
    updateUser.setNewPassword("oldPassword");

    when(userPostgresPort.getUserById(1L)).thenReturn(existingUser);

    userService.updatePassword(1L, updateUser);

    verify(userPostgresPort).getUserById(1L);
    verify(userPostgresPort, never()).updateUser(any());
  }

  @Test
  @DisplayName("Should get user successfully")
  void testGetUserSuccess() {
    when(userPostgresPort.getUserById(1L)).thenReturn(user);

    GetUserResponse response = userService.getUser(1L);

    assertThat(response).isEqualTo(getUserResponse);
    verify(userPostgresPort).getUserById(1L);
  }

  @Test
  @DisplayName("Should get users by name with partial match")
  void testGetUserByNameSuccess() {
    List<User> users = List.of(user);

    when(userPostgresPort.getUserByName("John")).thenReturn(users);

    List<GetUserResponse> result = userService.getUserByName("John");

    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(getUserResponse);
    verify(userPostgresPort).getUserByName("John");
  }

  @Test
  @DisplayName("Should delete user successfully")
  void testDeleteUserSuccess() {
    userService.deleteUser(1L);

    verify(userPostgresPort).deleteUser(1L);
  }
}
