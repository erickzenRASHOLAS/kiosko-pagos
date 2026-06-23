package cl.kiosko.ms_pagos.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF ya que usamos Microservicios Stateless con JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Configurar rutas públicas y protegidas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/auth/**").permitAll() // Endpoints de registro/login
                        .anyRequest().authenticated()                  // El resto requiere JWT válido
                )

                // Forzar que la sesión no guarde estado en el servidor (Stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Proveedor de datos de autenticación y orden del filtro inyectado
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
