package cl.kiosko.ms_pagos.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean// llama componentes externos (funciona como libreria)
    public OpenAPI customerOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("API 2026 Pagos kiosko")
                        .version("1.0")
                        .description("Documentación de la API para el microservicio de Pagos del kiosko"));
    }
}
