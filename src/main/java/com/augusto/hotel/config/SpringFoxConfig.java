package com.augusto.hotel.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
public class SpringFoxConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Last Hotel API")
                .description("API Documentation")
                .version("0.0.1")
                .build();
    }

    @Bean
    public Docket docket() {
        TypeResolver typeResolver = new TypeResolver();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(this.apiInfo())
                .enable(true)
                .alternateTypeRules(
                        newRule(typeResolver.resolve(List.class, LocalDate.class),
                                typeResolver.resolve(List.class, java.util.Date.class)))
                .select()
                .paths(PathSelectors.any())
                .build();
    }
}
