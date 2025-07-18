package com.example.association.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI employeeAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Employee Management API")
                        .description("Spring Boot REST API for managing Employees and Addresses")
                        .version("1.0.0"));
    }
}
