package com.rouandi.rouandi.controllers;
import com.rouandi.rouandi.DTOs.AuthDTO.AuthRequest;
import com.rouandi.rouandi.services.auth.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
public class AuthentificationController {
    private AuthService authService;

    public AuthentificationController(AuthService authService) {

        this.authService=authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }


}
