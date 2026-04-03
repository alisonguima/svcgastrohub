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
    public static final String USER_NOT_FOUND_WITH_ID = "User not found with id: ";
    public static final String USER_NOT_FOUND_WITH_NAME = "User not found with name: ";

    public static final String USER_TYPE_REQUIRED = "User type is required";

    public static final String ADDRESS_REQUIRED = "Address is required";

    // -------------------------------------------------------------------------
    // Conflict messages — User
    // -------------------------------------------------------------------------
    public static final String EMAIL_ALREADY_EXISTS = "Email already in use";
    public static final String LOGIN_ALREADY_EXISTS = "Login already in use";

    // -------------------------------------------------------------------------
    // ProblemDetail — Error handling
    // -------------------------------------------------------------------------
    public static final String PROBLEM_DETAIL_TYPE_BASE = "https://api.gastrohub.com/errors/";

    // ...existing code...
    public static final String ERROR_TYPE_DUPLICATE_RESOURCE = "duplicate-resource";
    public static final String ERROR_TYPE_RESOURCE_NOT_FOUND = "resource-not-found";
    public static final String ERROR_TYPE_INVALID_REQUEST = "invalid-request";
    public static final String ERROR_TYPE_CONFLICT = "conflict";
    public static final String ERROR_TYPE_VALIDATION = "validation-error";
    public static final String ERROR_TYPE_INTERNAL_SERVER = "internal-server-error";

    // Error titles
    public static final String ERROR_TITLE_DUPLICATE_RESOURCE = "Duplicate Resource";
    public static final String ERROR_TITLE_RESOURCE_NOT_FOUND = "Resource Not Found";
    public static final String ERROR_TITLE_INVALID_REQUEST = "Invalid Request";
    public static final String ERROR_TITLE_UNPROCESSABLE_ENTITY = "Unprocessable Entity";
    public static final String ERROR_TITLE_VALIDATION = "Validation Error";
    public static final String ERROR_TITLE_INTERNAL_SERVER = "Internal Server Error";

    // Error details
    public static final String ERROR_DETAIL_VALIDATION = "One or more fields have validation errors";
    public static final String ERROR_DETAIL_INTERNAL_SERVER = "An unexpected error occurred. Please try again later.";
}
