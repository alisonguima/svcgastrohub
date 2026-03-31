package com.restaurant.gastrohub.adapter.output.model;

import com.restaurant.gastrohub.application.domain.enums.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserEntity Tests")
class UserEntityTest {

    @Test
    @DisplayName("onCreate should encode password and set lastUpdateAt")
    void onCreate_shouldEncodePasswordAndSetLastUpdateAt() {
        // Arrange
        String plainPassword = "plainPassword";
        UserEntity userEntity = UserEntity.builder()
                .name("Test User")
                .email("test@example.com")
                .login("testlogin")
                .password(plainPassword)
                .userType(UserType.CUSTOMER)
                .address("Test Address")
                .build();

        // Act
        userEntity.onCreate();

        // Assert
        assertThat(userEntity.getPassword()).isNotEqualTo(plainPassword); // Password should be encoded
        assertThat(userEntity.getLastUpdateAt()).isNotNull(); // lastUpdateAt should be set
    }

    @Test
    @DisplayName("onUpdate should update lastUpdateAt")
    void onUpdate_shouldUpdateLastUpdateAt() {
        // Arrange
        UserEntity userEntity = UserEntity.builder()
                .name("Test User")
                .email("test@example.com")
                .login("testlogin")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .address("Test Address")
                .lastUpdateAt("oldTime")
                .build();

        // Act
        userEntity.onUpdate();

        // Assert
        assertThat(userEntity.getLastUpdateAt()).isNotEqualTo("oldTime"); // lastUpdateAt should be updated
        assertThat(userEntity.getLastUpdateAt()).isNotNull();
    }
}
