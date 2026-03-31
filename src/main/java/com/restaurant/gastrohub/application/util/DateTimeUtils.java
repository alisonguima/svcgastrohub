package com.restaurant.gastrohub.application.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;

import static com.restaurant.gastrohub.application.domain.ApiConstants.UTC;

public final class DateTimeUtils {

  private DateTimeUtils() {}

  public static DateTimeFormatter getDateTimeFormatter() {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        .withResolverStyle(ResolverStyle.STRICT);
  }

  public static ZonedDateTime generateDateTimeZoneUTC() { return ZonedDateTime.now(UTC); }

  public static String getDateTimeZoneUTC() {
    return generateDateTimeZoneUTC().format(getDateTimeFormatter());
  }

}
