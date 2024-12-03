package com.noom.interview.fullstack.sleep.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


@Configuration
public class SwaggerConfiguration {

    private final String applicationName;

    private final String timezone;

    public SwaggerConfiguration(@Value("${spring.application.name}") String applicationName,
                                @Value("${spring.application.timezone}") String timezone) {
        this.applicationName = applicationName;
        this.timezone = timezone;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title(applicationName).version("1.0").description(String.format("API Documentation. Please, be aware that all of the time values are processed in %s timezone.", timezone)))
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION, new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name(HttpHeaders.AUTHORIZATION)
                                .description("Token for authorization is represented with BAse64 encoded user id. This approach mocks the authorization process and brings the concept of user. To fetch all of the available user and encoded ids invoke GET /users endpoint."))
                );
    }

}
