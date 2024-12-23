package com.hana4.keywordhanaro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringConfig implements WebMvcConfigurer {
	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes("bearerAuth",
					new SecurityScheme()
						.type(SecurityScheme.Type.HTTP)
						.scheme("bearer")
						.bearerFormat("JWT")))
			.info(info())
			.addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
	}

	private Info info() {
		return new Info()
			.version("0.1.0")
			.title("KeywordHanaro Api Spec.")
			.description("description");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("http://localhost:3000")
			.allowedHeaders("*")
			.allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
			.allowCredentials(true);
	}
}
