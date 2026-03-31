package com.restaurant.gastrohub.application.domain;

import java.time.ZoneId;

public class ApiConstants {

    private ApiConstants() {}

    // -------------------------------------------------------------------------
    // Timezones
    // -------------------------------------------------------------------------
    public static final ZoneId UTC = ZoneId.of("UTC");
    public static final ZoneId BRASILIA = ZoneId.of("America/Sao_Paulo");

    // -------------------------------------------------------------------------
    // Validation patterns
    // -------------------------------------------------------------------------
    public static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    // -------------------------------------------------------------------------
    // Validation messages — User
    // -------------------------------------------------------------------------
    public static final String NAME_REQUIRED = "Name is required";
    public static final String NAME_SIZE = "Name must be between 2 and 100 characters";

    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID = "Email must be valid";

    public static final String LOGIN_REQUIRED = "Login is required";
    public static final String LOGIN_SIZE = "Login must be between 3 and 50 characters";

    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String CUR_PASSWORD_REQUIRED = "Current password is required";
    public static final String NEW_PASSWORD_REQUIRED = "New password is required";
    public static final String PASSWORD_INVALID = "Password must be at least 8 characters and contain at least one uppercase letter, one lowercase letter, one number and one special character (@$!%*?&)";
    public static final String INVALID_PASSWORD = "Current password is incorrect";
    public static final String USER_NOT_FOUND = "User not found with id: ";

    public static final String USER_TYPE_REQUIRED = "User type is required";
    public static final String USER_TYPE_INVALID = "Invalid user type. Valid types are: OWNER, CUSTOMER";

    public static final String ADDRESS_REQUIRED = "Address is required";

    // -------------------------------------------------------------------------
    // Conflict messages — User
    // -------------------------------------------------------------------------
    public static final String EMAIL_ALREADY_EXISTS = "Email already in use";
    public static final String LOGIN_ALREADY_EXISTS = "Login already in use";
}
