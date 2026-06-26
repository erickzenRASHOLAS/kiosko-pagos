package cl.kiosko.ms_pagos.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF ya que usamos Microservicios Stateless con JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Configurar rutas públicas y protegidas
                .authorizeHttpRequests(auth -> auth
                        // SE ELIMINÓ: "/v1/auth/**" ya que no habrá registro/login aquí
                        .requestMatchers("/doc/**").permitAll()         // Swagger UI
                        .requestMatchers("/v3/api-docs/**").permitAll() // OpenAPI Docs
                        .anyRequest().authenticated()                  // Todo lo demás (pagos, transacciones) requiere JWT válido
                )

                // Forzar que la sesión no guarde estado en el servidor (Stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // El filtro JWT interceptará las peticiones para validar el token de ms_usuarios
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}