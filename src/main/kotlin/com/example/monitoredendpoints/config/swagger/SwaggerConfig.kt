package com.example.monitoredendpoints.config.swagger

import org.springframework.context.annotation.*
import org.springframework.security.core.*
import springfox.documentation.builders.*
import springfox.documentation.service.*
import springfox.documentation.spi.*
import springfox.documentation.spi.service.contexts.*
import springfox.documentation.spring.web.plugins.*
import springfox.documentation.swagger2.annotations.*
import java.util.Collections.*


@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api() = Docket(DocumentationType.SWAGGER_2)
            .securitySchemes(listOf(ApiKey("apiKey", "Authorization", "header")))
            .securityContexts(listOf(
                    SecurityContext.builder()
                            .securityReferences(
                                    singletonList(SecurityReference.builder()
                                            .reference("apiKey")
                                            .scopes(listOf<AuthorizationScope>().toTypedArray())
                                            .build()
                                    )
                            )
                            .build())
            )
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.example.monitoredendpoints"))
            .paths(PathSelectors.any())
            .build()
            .ignoredParameterTypes(Authentication::class.java)
            .apiInfo(ApiInfoBuilder().title("Monitored-endpoints")
                    .description("example")
                    .version("1.0")
                    .contact(Contact("Ondrej Schrek", "www.example.com", "ondrej.schrek@gmail.com"))
                    .build()
            )
}