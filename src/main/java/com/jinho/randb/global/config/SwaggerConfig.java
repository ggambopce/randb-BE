package com.jinho.randb.global.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;

@OpenAPIDefinition(
        info = @Info(title = "Red & Blue API 명세서",
                description = "백엔드 API 서버",
                version = "v2")
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI3() {

        // OpenAPI 객체 생성 및 구성
        return new OpenAPI()
                .components(new Components());
    }

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/api/**"};

        return GroupedOpenApi.builder()
                .group("Red and Blue API v2")
                .pathsToMatch(paths)
                .build();
    }

    public SwaggerConfig(MappingJackson2HttpMessageConverter converter) {
        var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "octet-stream"));
        converter.setSupportedMediaTypes(supportedMediaTypes);
    }
}
