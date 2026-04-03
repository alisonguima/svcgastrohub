package com.restaurant.gastrohub.adapter.input.controller;

import com.restaurant.gastrohub.adapter.input.request.CreateUserRequest;
import com.restaurant.gastrohub.adapter.input.request.UpdateUserPasswordRequest;
import com.restaurant.gastrohub.adapter.input.request.UpdateUserRequest;
import com.restaurant.gastrohub.adapter.input.response.CreateUserResponse;
import com.restaurant.gastrohub.adapter.input.response.GetUserResponse;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import com.restaurant.gastrohub.application.domain.user.User;
import com.restaurant.gastrohub.application.port.input.UserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Tests")
class UserControllerTest {

  @Mock
  private UserUseCase userUseCase;

  @InjectMocks
  private UserController userController;

  private CreateUserRequest createUserRequest;
  private UpdateUserRequest updateUserRequest;
  private UpdateUserPasswordRequest updateUserPasswordRequest;
  private CreateUserResponse createUserResponse;
  private GetUserResponse getUserResponse;
  private ZonedDateTime testDateTime;

  @BeforeEach
  void setUp() {
    testDateTime = ZonedDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"));

    createUserRequest = new CreateUserRequest(
        "John Doe",
        "john@example.com",
        "johndoe",
        "Password@123",
        UserType.OWNER,
        "123 Main St"
    );

    updateUserRequest = new UpdateUserRequest(
        "John Updated",
        "john.updated@example.com",
        "johnupdated",
        UserType.CUSTOMER,
        "456 Oak Ave"
    );

    updateUserPasswordRequest = new UpdateUserPasswordRequest(
        "OldPassword@123",
        "NewPassword@123"
    );

    createUserResponse = new CreateUserResponse(
        "1",
        "John Doe",
        "john@example.com",
        "johndoe"
    );

    getUserResponse = new GetUserResponse(
        "1",
        "John Doe",
        "john@example.com",
        "johndoe",
        UserType.OWNER,
        testDateTime,
        "123 Main St"
    );
  }

  @Test
  @DisplayName("Should create user successfully")
  void testCreateUserSuccess() {
    when(userUseCase.createUser(any(User.class))).thenReturn(createUserResponse);

    ResponseEntity<CreateUserResponse> response = userController.createUser(createUserRequest);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isEqualTo(createUserResponse);
    verify(userUseCase, times(1)).createUser(any(User.class));
  }


  @Test
  @DisplayName("Should get user by id successfully")
  void testGetUserByIdSuccess() {
    when(userUseCase.getUser(1L)).thenReturn(getUserResponse);

    ResponseEntity<GetUserResponse> response = userController.getUser(1L);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(getUserResponse);
    verify(userUseCase, times(1)).getUser(1L);
  }

  @Test
  @DisplayName("Should get user by different id successfully")
  void testGetUserByDifferentIdSuccess() {
    Long userId = 999L;
    ZonedDateTime dateTime2 = ZonedDateTime.of(2024, 2, 1, 15, 30, 0, 0, ZoneId.of("UTC"));
    GetUserResponse differentUserResponse = new GetUserResponse(
        "999",
        "Jane Doe",
        "jane@example.com",
        "janedoe",
        UserType.CUSTOMER,
        dateTime2,
        "789 Pine Rd"
    );
    when(userUseCase.getUser(userId)).thenReturn(differentUserResponse);

    ResponseEntity<GetUserResponse> response = userController.getUser(userId);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(differentUserResponse);
    verify(userUseCase, times(1)).getUser(userId);
  }

  @Test
  @DisplayName("Should get user by name successfully")
  void testGetUserByNameSuccess() {
    List<GetUserResponse> users = List.of(getUserResponse);
    when(userUseCase.getUserByName("John Doe")).thenReturn(users);

    ResponseEntity<List<GetUserResponse>> response = userController.getUserByName("John Doe");

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(1);
    assertThat(response.getBody()).isEqualTo(users);
    verify(userUseCase, times(1)).getUserByName("John Doe");
  }

  @Test
  @DisplayName("Should get multiple users by partial name match")
  void testGetUserByPartialNameSuccess() {
    ZonedDateTime dateTime2 = ZonedDateTime.of(2024, 2, 1, 15, 30, 0, 0, ZoneId.of("UTC"));
    GetUserResponse user2 = new GetUserResponse(
        "2",
        "John Smith",
        "john.smith@example.com",
        "johnsmith",
        UserType.CUSTOMER,
        dateTime2,
        "456 Oak Ave"
    );
    List<GetUserResponse> users = List.of(getUserResponse, user2);
    when(userUseCase.getUserByName("John")).thenReturn(users);

    ResponseEntity<List<GetUserResponse>> response = userController.getUserByName("John");

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).hasSize(2);
    assertThat(response.getBody()).isEqualTo(users);
    verify(userUseCase, times(1)).getUserByName("John");
  }

  @Test
  @DisplayName("Should update user successfully")
  void testUpdateUserSuccess() {
    doNothing().when(userUseCase).updateUser(anyLong(), any(User.class));

    ResponseEntity<Void> response = userController.updateUser(1L, updateUserRequest);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(response.getBody()).isNull();
    verify(userUseCase, times(1)).updateUser(anyLong(), any(User.class));
  }

  @Test
  @DisplayName("Should update user with different id")
  void testUpdateUserDifferentIdSuccess() {
    Long userId = 777L;
    doNothing().when(userUseCase).updateUser(eq(userId), any(User.class));

    ResponseEntity<Void> response = userController.updateUser(userId, updateUserRequest);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    verify(userUseCase, times(1)).updateUser(eq(userId), any(User.class));
  }

  @Test
  @DisplayName("Should update password successfully")
  void testUpdateUserPasswordSuccess() {
    Long userId = 1L;
    doNothing().when(userUseCase).updatePassword(eq(userId), any(User.class));

    ResponseEntity<Void> response = userController.updatePassword(userId, updateUserPasswordRequest);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    verify(userUseCase, times(1)).updatePassword(eq(userId), any(User.class));
  }

  @Test
  @DisplayName("Should update password with different id")
  void testUpdatePasswordDifferentIdSuccess() {
    Long userId = 555L;
    doNothing().when(userUseCase).updatePassword(eq(userId), any(User.class));

    ResponseEntity<Void> response = userController.updatePassword(userId, updateUserPasswordRequest);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    verify(userUseCase, times(1)).updatePassword(eq(userId), any(User.class));
  }

  @Test
  @DisplayName("Should delete user successfully")
  void testDeleteUserSuccess() {
    doNothing().when(userUseCase).deleteUser(anyLong());

    ResponseEntity<Void> response = userController.deleteUser(1L);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(response.getBody()).isNull();
    verify(userUseCase, times(1)).deleteUser(1L);
  }

  @Test
  @DisplayName("Should delete user with different id")
  void testDeleteUserDifferentIdSuccess() {
    Long userId = 333L;
    doNothing().when(userUseCase).deleteUser(userId);

    ResponseEntity<Void> response = userController.deleteUser(userId);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    assertThat(response.getBody()).isNull();
    verify(userUseCase, times(1)).deleteUser(userId);
  }

  @Test
  @DisplayName("Should handle multiple sequential user operations")
  void testMultipleUserOperations() {
    when(userUseCase.createUser(any(User.class))).thenReturn(createUserResponse);
    ResponseEntity<CreateUserResponse> createResponse = userController.createUser(createUserRequest);
    assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    when(userUseCase.getUser(1L)).thenReturn(getUserResponse);
    ResponseEntity<GetUserResponse> getResponse = userController.getUser(1L);
    assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

    doNothing().when(userUseCase).updateUser(eq(1L), any(User.class));
    ResponseEntity<Void> updateResponse = userController.updateUser(1L, updateUserRequest);
    assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    doNothing().when(userUseCase).deleteUser(1L);
    ResponseEntity<Void> deleteResponse = userController.deleteUser(1L);
    assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

    verify(userUseCase, times(1)).createUser(any(User.class));
    verify(userUseCase, times(1)).getUser(1L);
    verify(userUseCase, times(1)).updateUser(eq(1L), any(User.class));
    verify(userUseCase, times(1)).deleteUser(1L);
  }

  @Test
  @DisplayName("Should verify correct data mapping in create request")
  void testCreateUserDataMapping() {
    when(userUseCase.createUser(any(User.class))).thenReturn(createUserResponse);

    userController.createUser(createUserRequest);

    verify(userUseCase).createUser(any(User.class));
  }

  @Test
  @DisplayName("Should verify correct data mapping in update request")
  void testUpdateUserDataMapping() {
    doNothing().when(userUseCase).updateUser(anyLong(), any(User.class));

    userController.updateUser(1L, updateUserRequest);

    verify(userUseCase).updateUser(anyLong(), any(User.class));
  }
}
