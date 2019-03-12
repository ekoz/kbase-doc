/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年12月2日 上午11:39:23
 * @version 1.0
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
	
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.eastrobot.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private List<SecurityScheme> securitySchemes() {
        return newArrayList(new ApiKey(SystemConstants.API_KEY, SystemConstants.API_TOKEN, SystemConstants.API_PASSAS));
    }

    private List<SecurityContext> securityContexts() {
        return newArrayList(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .forPaths(PathSelectors.any())
                        .build()
        );
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return newArrayList(new SecurityReference(SystemConstants.API_KEY, authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot 中使用 Swagger2 构建 RESTful APIs ")
                .description("")
                .termsOfServiceUrl("http://ekozhan.com/kbase-doc/")
                .contact(new Contact("ekozhan", "http://ekozhan.com/", "eko.z@outlook.com"))
                .version("1.0")
                .build();
    }

}
