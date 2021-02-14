package com.cybertek.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI customOpenApi(){
        SecurityScheme securityItem = new SecurityScheme();
        securityItem.setType(SecurityScheme.Type.HTTP);
        securityItem.setScheme("bearer");
        securityItem.setBearerFormat("JWT");
        securityItem.setIn(SecurityScheme.In.HEADER);
        securityItem.setName("Authorization");
        Info infoVersion=new Info().title("Ticketing application").version("snapshot");
        SecurityRequirement seecurityRequirement=new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read","write"));
        return new OpenAPI()
                .components(new Components()
                .addSecuritySchemes("bearer-jwt",securityItem))
                .info(infoVersion)
                .addSecurityItem(seecurityRequirement);

    }
}
