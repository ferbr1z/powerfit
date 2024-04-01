package com.devs.powerfit.services.auth;

import com.devs.powerfit.beans.auth.RolBean;
import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.daos.auth.RolDao;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.daos.empleados.EmpleadoDao;
import com.devs.powerfit.dtos.auth.UsuarioDto;
import com.devs.powerfit.interfaces.auth.IAuthService;
import com.devs.powerfit.security.auth.AuthRequest;
import com.devs.powerfit.security.auth.AuthResponse;
import com.devs.powerfit.security.jwt.JWTService;
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

    @Autowired
    private EmpleadoDao empleadoDao;

    @Autowired
    private ClienteDao clienteDao;

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

    public ResponseEntity<?> update(String email, UsuarioDto request) {
        try {
            Optional<UsuarioBean> optionalUser = userDao.findByEmailAndActiveIsTrue(email);

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            UsuarioBean user = optionalUser.get();

            if (request.getNombre() != null && !request.getNombre().isEmpty()) {
                user.setNombre(request.getNombre());
            }

            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                user.setEmail(request.getEmail());
            }

            if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            if (request.getRol_id() != null) {
                Optional<RolBean> rol = rolDao.findByIdAndActiveTrue(request.getRol_id());
                if (rol.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Rol no encontrado");
                }
                user.setRol(rol.get());
            }

            userDao.save(user);

            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No se pudo actualizar el usuario");
        }



    }

    public ResponseEntity<?> getByEmail(String email) {
        try {
            Optional<UsuarioBean> optionalUser = userDao.findByEmailAndActiveIsTrue(email);

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }

            UsuarioBean user = optionalUser.get();
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el usuario por email");
        }
    }

    public ResponseEntity<?> delete(String email){
        try {
            Optional<UsuarioBean> optionalUser = userDao.findByEmailAndActiveIsTrue(email);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            }
                UsuarioBean user = optionalUser.get();
                user.setActive(false);
                userDao.save(user);
                return ResponseEntity.ok().body("Eliminado con exito");

        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener el usuario por email");
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
            Optional<EmpleadoBean> empleadoBean = empleadoDao.findByEmailAndActiveIsTrue(request.getEmail());
            Optional<ClienteBean> clienteBean = clienteDao.findByEmail(request.getEmail());
            if (user.getRol().getId() == 2){
                final AuthResponse responseCliente = new AuthResponse(user.getEmail(), accessToken, rolName.get().getNombre(), user.getNombre(), clienteBean.get().getId());
                return ResponseEntity.ok().body(responseCliente);
            }
            final AuthResponse response = new AuthResponse(user.getEmail(), accessToken, rolName.get().getNombre(), user.getNombre(), empleadoBean.get().getId());
            return ResponseEntity.ok().body(response);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado o credenciales inválidas");
        }
    }
}
