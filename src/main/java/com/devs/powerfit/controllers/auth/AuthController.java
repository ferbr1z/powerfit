package com.devs.powerfit.controllers.auth;

import com.devs.powerfit.dtos.auth.UsuarioDto;
import com.devs.powerfit.security.auth.AuthRequest;
import com.devs.powerfit.services.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UsuarioDto request){
        return authService.register(request);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verify(HttpServletRequest request){
        var token = request.getHeader("Authorization").substring(7);
        return ResponseEntity.ok(authService.isTokenValid(token));
    }

}



