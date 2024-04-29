package com.devs.powerfit.utils;

import java.util.List;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenAPIConfig {

    @Value("${openapi.url}")
    private String devUrl;

    @Value("${openapi.testing.url}")
    private String testUrl;

    @Value("${version}")
    private String version;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Localhost");

        Server testServer = new Server();
        testServer.setUrl(testUrl);
        testServer.setDescription("Usando https://pwf-bcknd-tst.up.railway.app para testear");


        Info info = new Info()
                .title("PowerFit API")
                .version(version);

        SecurityScheme bearerScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        return new OpenAPI().info(info).servers(List.of(devServer, testServer))
                .components(new io.swagger.v3.oas.models.Components().addSecuritySchemes("bearer-jwt", bearerScheme));
    }
}