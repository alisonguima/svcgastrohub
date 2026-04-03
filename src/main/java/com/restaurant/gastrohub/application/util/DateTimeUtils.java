package com.restaurant.gastrohub.application.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import static com.restaurant.gastrohub.application.domain.ApiConstants.UTC;

public final class DateTimeUtils {

  private DateTimeUtils() {}

  public static ZonedDateTime generateDateTimeZoneUTC() { return ZonedDateTime.now(UTC); }

  public static String getProblemDetailTimestamp() {
    DateTimeFormatter problemDetailFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
        .withResolverStyle(ResolverStyle.STRICT);
    return ZonedDateTime.now(UTC).format(problemDetailFormatter);
  }

}
