package com.devs.powerfit.controllers.auth;

import com.devs.powerfit.security.password.ForgotPasswordRequest;
import com.devs.powerfit.security.password.PasswordChangeRequest;
import com.devs.powerfit.security.password.PasswordRecoveryReq;
import com.devs.powerfit.services.auth.PasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/password")
public class PasswordController {
    private PasswordService _passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this._passwordService = passwordService;
    }

    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest newPassword, Principal principal) {

        _passwordService.changePassword(newPassword, principal);
        return ResponseEntity.ok().body(Map.of("message", "La contraseña se cambió con éxito"));
    }

    @PostMapping("/forgot")
    public ResponseEntity<?> forgotPassword(@RequestBody @Valid ForgotPasswordRequest email) {
        _passwordService.forgotPassword(email.getEmail());
        return ResponseEntity.ok().body(Map.of("message", "Se ha enviado un correo con las instrucciones para restablecer la contraseña"));
    }

    @GetMapping("/reset/{token}")
    public ResponseEntity<?> validateToken(@PathVariable String token) {
        _passwordService.verifyTokenValidation(token);
        return ResponseEntity.ok().body(Map.of("message", "El token es válido."));
    }

    @PostMapping("/reset/{token}")
    public ResponseEntity<?> resetPassword(@PathVariable String token, @RequestBody PasswordRecoveryReq passwordRecoveryReq) {
        _passwordService.recoveryPassword(token, passwordRecoveryReq.getPassword());
        return ResponseEntity.ok().body(Map.of("message", "Se ha restablecido la contraseña con éxito."));
    }


}
