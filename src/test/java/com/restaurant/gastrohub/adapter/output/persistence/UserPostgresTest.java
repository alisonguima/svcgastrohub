package com.restaurant.gastrohub.adapter.output.persistence;

import com.restaurant.gastrohub.adapter.output.model.UserEntity;
import com.restaurant.gastrohub.adapter.output.persistence.repository.UserRepository;
import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.user.User;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import com.restaurant.gastrohub.application.exception.DefaultException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserPostgres Tests")
class UserPostgresTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserPostgres userPostgres;

    private UserEntity createUserEntity() {
        return UserEntity.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .login("testlogin")
                .password("password")
                .userType(UserType.CUSTOMER)
                .build();
    }

    private User createUser() {
        return User.builder()
                .id(1L)
                .name("Test User")
                .email("test@example.com")
                .login("testlogin")
                .password("password")
                .userType(UserType.CUSTOMER)
                .build();
    }

    @Test
    @DisplayName("saveUser should save and return mapped user")
    void saveUser_shouldSaveAndReturnMappedUser() {
        // Arrange
        UserEntity userEntity = createUserEntity();
        User expectedUser = createUser();
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        // Act
        User result = userPostgres.saveUser(userEntity);

        // Assert
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedUser);
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("updateUser should update existing user")
    void updateUser_shouldUpdateExistingUser() {
        // Arrange
        UserEntity userEntity = createUserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);

        // Act
        userPostgres.updateUser(userEntity);

        // Assert
        verify(userRepository).findById(1L);
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("updateUser should throw exception when user not found")
    void updateUser_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        UserEntity userEntity = createUserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userPostgres.updateUser(userEntity))
                .isInstanceOf(DefaultException.class)
                .hasMessage(ApiConstants.USER_NOT_FOUND_WITH_ID + 1L);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("existsByLogin should return true when login exists")
    void existsByLogin_shouldReturnTrueWhenLoginExists() {
        // Arrange
        String login = "testlogin";
        when(userRepository.existsByLogin(login)).thenReturn(true);

        // Act
        boolean result = userPostgres.existsByLogin(login);

        // Assert
        assertThat(result).isTrue();
        verify(userRepository).existsByLogin(login);
    }

    @Test
    @DisplayName("existsByLogin should return false when login does not exist")
    void existsByLogin_shouldReturnFalseWhenLoginDoesNotExist() {
        // Arrange
        String login = "testlogin";
        when(userRepository.existsByLogin(login)).thenReturn(false);

        // Act
        boolean result = userPostgres.existsByLogin(login);

        // Assert
        assertThat(result).isFalse();
        verify(userRepository).existsByLogin(login);
    }

    @Test
    @DisplayName("existsByEmail should return true when email exists")
    void existsByEmail_shouldReturnTrueWhenEmailExists() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = userPostgres.existsByEmail(email);

        // Assert
        assertThat(result).isTrue();
        verify(userRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("existsByEmail should return false when email does not exist")
    void existsByEmail_shouldReturnFalseWhenEmailDoesNotExist() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean result = userPostgres.existsByEmail(email);

        // Assert
        assertThat(result).isFalse();
        verify(userRepository).existsByEmail(email);
    }

    @Test
    @DisplayName("getUserById should return user when found")
    void getUserById_shouldReturnUserWhenFound() {
        // Arrange
        UserEntity userEntity = createUserEntity();
        User expectedUser = createUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        // Act
        User result = userPostgres.getUserById(1L);

        // Assert
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedUser);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("getUserById should throw exception when user not found")
    void getUserById_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userPostgres.getUserById(1L))
                .isInstanceOf(DefaultException.class)
                .hasMessage(ApiConstants.USER_NOT_FOUND_WITH_ID + 1L);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("deleteUser should delete existing user")
    void deleteUser_shouldDeleteExistingUser() {
        // Arrange
        UserEntity userEntity = createUserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userPostgres.deleteUser(1L);

        // Assert
        verify(userRepository).findById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteUser should throw exception when user not found")
    void deleteUser_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userPostgres.deleteUser(1L))
                .isInstanceOf(DefaultException.class)
                .hasMessage(ApiConstants.USER_NOT_FOUND_WITH_ID + 1L);
        verify(userRepository).findById(1L);
    }

    @Test
    @DisplayName("getUserByName should return list of users with partial name match")
    void getUserByName_shouldReturnListOfUsersWithPartialNameMatch() {
        // Arrange
        UserEntity userEntity1 = UserEntity.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .login("johndoe")
                .password("password")
                .userType(UserType.OWNER)
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .id(2L)
                .name("John Smith")
                .email("john.smith@example.com")
                .login("johnsmith")
                .password("password")
                .userType(UserType.CUSTOMER)
                .build();

        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(userEntities);

        // Act
        List<User> result = userPostgres.getUserByName("John");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");
        assertThat(result.get(1).getName()).isEqualTo("John Smith");
        verify(userRepository).findByNameContainingIgnoreCase("John");
    }

    @Test
    @DisplayName("getUserByName should return single user when searching by exact name")
    void getUserByName_shouldReturnSingleUserWhenSearchingByExactName() {
        // Arrange
        UserEntity userEntity = createUserEntity();
        List<UserEntity> userEntities = List.of(userEntity);
        when(userRepository.findByNameContainingIgnoreCase("Test User")).thenReturn(userEntities);

        // Act
        List<User> result = userPostgres.getUserByName("Test User");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test User");
        verify(userRepository).findByNameContainingIgnoreCase("Test User");
    }

    @Test
    @DisplayName("getUserByName should return empty list when no users found")
    void getUserByName_shouldReturnEmptyListWhenNoUsersFound() {
        // Arrange
        when(userRepository.findByNameContainingIgnoreCase("NonExistent")).thenReturn(List.of());

        // Act
        List<User> result = userPostgres.getUserByName("NonExistent");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(userRepository).findByNameContainingIgnoreCase("NonExistent");
    }

    @Test
    @DisplayName("getUserByName should be case-insensitive")
    void getUserByName_shouldBeCaseInsensitive() {
        // Arrange
        UserEntity userEntity = createUserEntity();
        List<UserEntity> userEntities = List.of(userEntity);
        when(userRepository.findByNameContainingIgnoreCase("test user")).thenReturn(userEntities);

        // Act
        List<User> result = userPostgres.getUserByName("test user");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test User");
        verify(userRepository).findByNameContainingIgnoreCase("test user");
    }

    @Test
    @DisplayName("getUserByName should return multiple users with partial match")
    void getUserByName_shouldReturnMultipleUsersWithPartialMatch() {
        // Arrange
        UserEntity userEntity1 = UserEntity.builder()
                .id(1L)
                .name("Alison Guimaraes")
                .email("alison@example.com")
                .login("alison")
                .password("password")
                .userType(UserType.OWNER)
                .build();

        UserEntity userEntity2 = UserEntity.builder()
                .id(2L)
                .name("Alison Silva")
                .email("alison.silva@example.com")
                .login("alisonsilva")
                .password("password")
                .userType(UserType.CUSTOMER)
                .build();

        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);
        when(userRepository.findByNameContainingIgnoreCase("Alison")).thenReturn(userEntities);

        // Act
        List<User> result = userPostgres.getUserByName("Alison");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(user -> user.getName().contains("Alison"));
        verify(userRepository).findByNameContainingIgnoreCase("Alison");
    }
}
