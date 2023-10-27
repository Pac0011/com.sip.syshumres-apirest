package com.sip.syshumres_apirest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	public static final String USER_TAG = "UserController";

	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
                .enable(true)
                .apiInfo(new ApiInfoBuilder()
                        .title("Swagger Super")
                        .description("Swagger Description details")
                        .version("1.0").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sip.syshumres_apirest.controllers"))
                .paths(PathSelectors.any()).build()
                .tags(new Tag(USER_TAG, "REST APIs related to User Entity"));
	}
	
}
