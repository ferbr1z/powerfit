package com.devs.powerfit.services;

import com.devs.powerfit.beans.RolBean;
import com.devs.powerfit.beans.UsuarioBean;
import com.devs.powerfit.daos.RolDao;
import com.devs.powerfit.daos.UsuarioDao;
import com.devs.powerfit.dtos.UsuarioDto;
import com.devs.powerfit.interfaces.IAuthService;
import com.devs.powerfit.security.AuthRequest;
import com.devs.powerfit.security.AuthResponse;
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
import org.springframework.security.core.AuthenticationException;
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

    public ResponseEntity<?> register(UsuarioDto request){
        LOGGER.error(request.toString());
        try {
            if(userDao.findByEmailAndActiveIsTrue(request.getEmail()).isPresent()){
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

            final AuthResponse response = new AuthResponse(user.getEmail(), accessToken, user.getRol().getId(), user.getNombre());
            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (AuthenticationException ex) {
            // Si la autenticación falla por otra razón que no sea credenciales incorrectas
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado o credenciales inválidas");
        }
    }



}
