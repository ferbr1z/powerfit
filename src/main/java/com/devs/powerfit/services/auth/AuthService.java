package com.devs.powerfit.services.auth;

import com.devs.powerfit.beans.auth.RolBean;
import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.daos.auth.RolDao;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.dtos.auth.UsuarioDto;
import com.devs.powerfit.interfaces.auth.IAuthService;
import com.devs.powerfit.security.auth.AuthRequest;
import com.devs.powerfit.security.auth.AuthResponse;
import com.devs.powerfit.security.jwt.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class AuthService implements IAuthService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    UsuarioDao userDao;
    @Autowired
    RolDao rolDao;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;

    public ResponseEntity<?> register(UsuarioDto request) {
        try {
            if (request.getNombre().isEmpty() || request.getPassword().isEmpty() || request.getEmail().isEmpty() || request.getRol_id() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos los campos son obligatorios para registrar un nuevo usuario.");
            }
            if (userDao.findByEmailAndActiveIsTrue(request.getEmail()).isPresent() || userDao.findByNombreAndActiveIsTrue(request.getNombre()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El usuario ya existe");
            }
            Optional<RolBean> rol = rolDao.findByIdAndActiveTrue(request.getRol_id());
            if (rol.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rol no encontrado");
            }
            UsuarioBean newUser = new UsuarioBean();
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setNombre(request.getNombre());
            newUser.setRol(rol.get());
            newUser.setActive(true);
            userDao.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo crear el usuario");
        }
    }

    public ResponseEntity<?> login(AuthRequest request) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            final UsuarioBean user = (UsuarioBean) authentication.getPrincipal();
            final String accessToken = jwtService.generateToken(user);
            Optional<RolBean> rolName = rolDao.findByIdAndActiveTrue(user.getRol().getId());

            final AuthResponse response = new AuthResponse(user.getEmail(), accessToken, rolName.get().getNombre(), user.getNombre());
            return ResponseEntity.ok().body(response);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado o credenciales inválidas");
        }
    }
}
