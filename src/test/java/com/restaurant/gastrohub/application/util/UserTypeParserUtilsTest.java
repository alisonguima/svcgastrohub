package com.restaurant.gastrohub.application.util;

import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import com.restaurant.gastrohub.application.exception.DefaultException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserTypeParserUtils Tests")
class UserTypeParserUtilsTest {

    @Test
    @DisplayName("parse should return OWNER for valid input 'OWNER'")
    void parse_shouldReturnOWNERForValidInputOWNER() {
        // Act
        UserType result = UserTypeParserUtils.parse("OWNER");

        // Assert
        assertThat(result).isEqualTo(UserType.OWNER);
    }

    @Test
    @DisplayName("parse should return OWNER for lowercase 'owner'")
    void parse_shouldReturnOWNERForLowercaseOwner() {
        // Act
        UserType result = UserTypeParserUtils.parse("owner");

        // Assert
        assertThat(result).isEqualTo(UserType.OWNER);
    }

    @Test
    @DisplayName("parse should return CUSTOMER for valid input 'CUSTOMER'")
    void parse_shouldReturnCUSTOMERForValidInputCUSTOMER() {
        // Act
        UserType result = UserTypeParserUtils.parse("CUSTOMER");

        // Assert
        assertThat(result).isEqualTo(UserType.CUSTOMER);
    }

    @Test
    @DisplayName("parse should return CUSTOMER for lowercase 'customer'")
    void parse_shouldReturnCUSTOMERForLowercaseCustomer() {
        // Act
        UserType result = UserTypeParserUtils.parse("customer");

        // Assert
        assertThat(result).isEqualTo(UserType.CUSTOMER);
    }

    @Test
    @DisplayName("parse should return OWNER for mixed case 'Owner'")
    void parse_shouldReturnOWNERForMixedCaseOwner() {
        // Act
        UserType result = UserTypeParserUtils.parse("Owner");

        // Assert
        assertThat(result).isEqualTo(UserType.OWNER);
    }

    @Test
    @DisplayName("parse should return CUSTOMER for mixed case 'Customer'")
    void parse_shouldReturnCUSTOMERForMixedCaseCustomer() {
        // Act
        UserType result = UserTypeParserUtils.parse("Customer");

        // Assert
        assertThat(result).isEqualTo(UserType.CUSTOMER);
    }

    @Test
    @DisplayName("parse should throw exception for null input")
    void parse_shouldThrowExceptionForNullInput() {
        // Act & Assert
        assertThatThrownBy(() -> UserTypeParserUtils.parse(null))
                .isInstanceOf(DefaultException.class)
                .hasMessage(ApiConstants.USER_TYPE_INVALID);
    }

    @Test
    @DisplayName("parse should throw exception for empty string")
    void parse_shouldThrowExceptionForEmptyString() {
        // Act & Assert
        assertThatThrownBy(() -> UserTypeParserUtils.parse(""))
                .isInstanceOf(DefaultException.class)
                .hasMessage(ApiConstants.USER_TYPE_INVALID);
    }

    @Test
    @DisplayName("parse should throw exception for whitespace only")
    void parse_shouldThrowExceptionForWhitespaceOnly() {
        // Act & Assert
        assertThatThrownBy(() -> UserTypeParserUtils.parse("   "))
                .isInstanceOf(DefaultException.class)
                .hasMessage(ApiConstants.USER_TYPE_INVALID);
    }

    @Test
    @DisplayName("parse should throw exception for invalid user type")
    void parse_shouldThrowExceptionForInvalidUserType() {
        // Act & Assert
        assertThatThrownBy(() -> UserTypeParserUtils.parse("INVALID"))
                .isInstanceOf(DefaultException.class)
                .hasMessage(ApiConstants.USER_TYPE_INVALID);
    }

    @Test
    @DisplayName("parse should throw exception for random string")
    void parse_shouldThrowExceptionForRandomString() {
        // Act & Assert
        assertThatThrownBy(() -> UserTypeParserUtils.parse("random"))
                .isInstanceOf(DefaultException.class)
                .hasMessage(ApiConstants.USER_TYPE_INVALID);
    }
}
