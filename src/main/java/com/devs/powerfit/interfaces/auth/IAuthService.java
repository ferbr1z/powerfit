package com.devs.powerfit.interfaces.auth;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.dtos.auth.UsuarioDto;
import com.devs.powerfit.security.auth.AuthRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthService {
    public ResponseEntity<?> register(UsuarioDto request);
    public ResponseEntity<?> login(AuthRequest request);

    public UsuarioBean getByEmail(String email);
}
