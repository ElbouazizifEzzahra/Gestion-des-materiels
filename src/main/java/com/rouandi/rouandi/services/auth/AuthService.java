package com.rouandi.rouandi.services.auth;

import com.rouandi.rouandi.DTOs.AuthDTO.AuthRequest;
import com.rouandi.rouandi.entities.User;
import com.rouandi.rouandi.jwt.JwtUtil;
import com.rouandi.rouandi.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service

public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> login(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword())
            );

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

            String token = jwtUtil.generateToken(user);
            String role = jwtUtil.extractRole(token);
/*
            String redirect = switch (role) {
                case "E" -> "/employe";
                case "ESI" -> "/employe_service_informatique";
                case "ADMIN" -> "/admin";
                default -> "/acces/denied";
            };

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("redirect", redirect);*/
            Map<String, String> response = new HashMap<>();
            response.put("token", token);


            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Email ou mot de passe incorrect");
        }
    }
}

