package com.restaurant.gastrohub.adapter.output.model;

import com.restaurant.gastrohub.application.domain.enums.UserType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserEntity Tests")
class UserEntityTest {

    @Test
    @DisplayName("onCreate should encode password and set lastUpdateAt")
    void onCreate_shouldEncodePasswordAndSetLastUpdateAt() {
        String plainPassword = "plainPassword";
        UserEntity userEntity = UserEntity.builder()
                .name("Test User")
                .email("test@example.com")
                .login("testlogin")
                .password(plainPassword)
                .userType(UserType.CUSTOMER)
                .address("Test Address")
                .build();

        userEntity.onCreate();

        assertThat(userEntity.getPassword()).isNotEqualTo(plainPassword);
        assertThat(userEntity.getLastUpdateAt()).isNotNull();
        assertThat(userEntity.getLastUpdateAt()).isInstanceOf(ZonedDateTime.class);
    }

    @Test
    @DisplayName("onUpdate should update lastUpdateAt")
    void onUpdate_shouldUpdateLastUpdateAt() throws InterruptedException {

        ZonedDateTime oldTime = ZonedDateTime.now(ZoneId.of("UTC")).minusSeconds(5);
        UserEntity userEntity = UserEntity.builder()
                .name("Test User")
                .email("test@example.com")
                .login("testlogin")
                .password("encodedPassword")
                .userType(UserType.CUSTOMER)
                .address("Test Address")
                .lastUpdateAt(oldTime)
                .build();

        Thread.sleep(10);


        userEntity.onUpdate();


        assertThat(userEntity.getLastUpdateAt()).isNotNull();
        assertThat(userEntity.getLastUpdateAt()).isAfter(oldTime);
    }
}
