package com.devs.powerfit.controllers.auth;

import com.devs.powerfit.security.password.PasswordChangeRequest;
import com.devs.powerfit.services.auth.PasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> changePassword(@RequestBody @Validgit a PasswordChangeRequest newPassword, Principal principal) {

        _passwordService.changePassword(newPassword, principal);
        return ResponseEntity.ok().body(Map.of("message", "La contraseña se cambió con éxito"));
    }

}
