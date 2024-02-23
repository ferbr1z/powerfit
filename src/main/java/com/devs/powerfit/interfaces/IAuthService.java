package com.devs.powerfit.interfaces;

import com.devs.powerfit.dtos.UsuarioDto;
import com.devs.powerfit.security.AuthRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    public ResponseEntity<?> register(UsuarioDto request);
    public ResponseEntity<?> login(AuthRequest request);
}
