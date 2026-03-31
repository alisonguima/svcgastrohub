package com.restaurant.gastrohub.application.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration of user types in the system")
public enum UserType {

    @Schema(description = "Restaurant owner with administrative privileges")
    OWNER,

    @Schema(description = "Regular customer user")
    CUSTOMER;
}
