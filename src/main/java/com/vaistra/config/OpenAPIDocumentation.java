package com.vaistra.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIDocumentation {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info().title("VAISTRA-MANAGEMENT-SYSTEM")
                        .contact(new Contact().name("Kalpesh Amlani")
                                .email("nadeempalkhiwala@gmail.com")
                                .url("https://vaistratechnologies.com/"))
                        .description("Vaistra Technologies CSCV Management")
                        .version("v1.0")
                        .termsOfService("Terms and Condition")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))

                .externalDocs(new ExternalDocumentation()
                        .description("Vaistra-Mangement Documentation")
                        .url("https://springshop.wiki.github.org/docs"));
    }

}
