package cl.kiosko.ms_pagos.Controller;
import cl.kiosko.ms_pagos.model.User;
import cl.kiosko.ms_pagos.model.Role;
import cl.kiosko.ms_pagos.repository.UserRepository;
import cl.kiosko.ms_pagos.security.JwtService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    // 1. ENDPOINT PARA REGISTRAR UN USUARIO NUEVO
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El email ya está registrado"));
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // Encripta la contraseña
                .role(Role.USER) // Asigna el rol que creamos antes
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
    // 2. ENDPOINT PARA INICIAR SESIÓN (LOGIN)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // Esto gatilla to do el proceso de Spring Security por debajo
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
@Data class RegisterRequest { private String email; private String password; }
@Data class LoginRequest { private String email; private String password; }