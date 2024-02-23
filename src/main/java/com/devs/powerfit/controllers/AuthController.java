package com.devs.powerfit.controllers;

import com.devs.powerfit.beans.RolBean;
import com.devs.powerfit.beans.UsuarioBean;
import com.devs.powerfit.daos.RolDao;
import com.devs.powerfit.daos.UsuarioDao;
import com.devs.powerfit.security.AplicationSecurity;
import com.devs.powerfit.security.AuthRequest;
import com.devs.powerfit.security.AuthResponse;
import com.devs.powerfit.security.jwt.JwtTokenUtil;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AplicationSecurity.class);
    @Autowired
    AuthenticationManager authManager;
    @Autowired
    JwtTokenUtil jwtUtil;
    @Autowired
    UsuarioDao userDao;
    @Autowired
    RolDao rolDao;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            final Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            final UsuarioBean user = (UsuarioBean) authentication.getPrincipal();
            final String accessToken = jwtUtil.generateAccesToken(user);

            final AuthResponse response = new AuthResponse(user.getEmail(), accessToken, user.getRol().getId(), user.getNombre());
            return ResponseEntity.ok().body(response);

        }catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid AuthRequest request){
        try {
            LOGGER.warn(request.toString());
            System.out.println(request.toString());
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
            newUser.setRol(rol.get());

            userDao.save(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo crear el usuario");
        }
    }

}



