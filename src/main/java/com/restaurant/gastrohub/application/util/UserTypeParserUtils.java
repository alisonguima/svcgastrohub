package com.restaurant.gastrohub.application.util;

import com.restaurant.gastrohub.application.domain.ApiConstants;
import com.restaurant.gastrohub.application.domain.enums.UserType;
import com.restaurant.gastrohub.application.exception.DefaultException;

import java.util.Arrays;
import java.util.Optional;

public final class UserTypeParserUtils {

  private UserTypeParserUtils() {}

  public static UserType parse(String userType) {
    return Optional.ofNullable(userType)
        .map(String::trim)
        .filter(type -> !type.isEmpty())
        .flatMap(type -> Arrays.stream(UserType.values())
            .filter(enumType -> enumType.name().equalsIgnoreCase(type))
            .findFirst())
        .orElseThrow(() -> new DefaultException(ApiConstants.USER_TYPE_INVALID));
  }
}

