package com.restaurant.gastrohub.application.util;

import com.restaurant.gastrohub.application.exception.DefaultException;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public final class ConflictValidatorUtils {

  private ConflictValidatorUtils() {}

  public static void throwIfExists(boolean exists, String logMessage, Object logArg, String errorMessage) {
    Optional.of(exists)
        .filter(Boolean::booleanValue)
        .ifPresent(e -> {
          log.warn(logMessage, logArg);
          throw new DefaultException(errorMessage);
        });
  }
}

