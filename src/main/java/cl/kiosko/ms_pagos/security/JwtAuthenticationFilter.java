package cl.kiosko.ms_pagos.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    // El constructor queda impecable: solo depende de JwtService, sin rastros de bases de datos de usuarios
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 1. Si no viene el token Bearer, se ignora y se continúa con el flujo normal
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extraer el string del JWT
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        // 3. Validar de forma puramente matemática/estática (sin ir a la Base de Datos)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 🔥 CORRECCIÓN: Quitamos la llamada a userDetailsService.
            // Ahora validamos el token únicamente verificando que la firma sea correcta y no haya expirado.
            if (jwtService.isTokenValid(jwt)) {

                // Creamos el objeto de autenticación usando el email extraído directamente del JWT
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail, // Este será el "Principal" que podrás inyectar en tus controladores de pagos
                        null,
                        Collections.emptyList() // Lista de roles vacía por defecto (puedes extraerlos del JWT si los necesitas)
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Autorizamos el paso en el contexto de Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}