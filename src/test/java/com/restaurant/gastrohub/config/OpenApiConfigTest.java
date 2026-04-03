package com.restaurant.gastrohub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiConfigTest {

    @Test
    @DisplayName("customOpenAPI should return OpenAPI with correct info")
    void customOpenAPI_shouldReturnOpenAPIWithCorrectInfo() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(OpenApiConfig.class);
        OpenApiConfig config = context.getBean(OpenApiConfig.class);

        OpenAPI openAPI = config.customOpenAPI();

        assertThat(openAPI).isNotNull();
        Info info = openAPI.getInfo();
        assertThat(info).isNotNull();
        assertThat(info.getTitle()).isEqualTo("GastroHub API");
        assertThat(info.getVersion()).isEqualTo("1.0.0");
        assertThat(info.getDescription()).isEqualTo("Unified backend system for a local restaurant chain, enabling efficient user management, authentication, and administrative operations in a containerized environment.");
        assertThat(info.getContact()).isNotNull();
        assertThat(info.getContact().getName()).isEqualTo("GastroHub Support");
        assertThat(info.getContact().getEmail()).isEqualTo("support@gastrohub.com");
        assertThat(info.getLicense()).isNotNull();
        assertThat(info.getLicense().getName()).isEqualTo("Apache 2.0");
        assertThat(info.getLicense().getUrl()).isEqualTo("http://www.apache.org/licenses/LICENSE-2.0");

        context.close();
    }
}
