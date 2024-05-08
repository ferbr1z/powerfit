package com.devs.powerfit.services.auth;

import com.devs.powerfit.beans.auth.PasswordRecoveryTokenBean;
import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.daos.auth.PasswordRecoveryTokenDao;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.security.password.PasswordChangeRequest;
import com.devs.powerfit.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordService {

    private PasswordEncoder _encoder;
    private UsuarioDao _userDao;
    private PasswordRecoveryTokenDao _recoveryTokeService;
    private EmailService _mail;

    @Value("${app.password.token.duration}")
    private Integer _tokenDuration;

    @Autowired
    public PasswordService(PasswordEncoder passwordEncoder, UsuarioDao userDao, EmailService mail, PasswordRecoveryTokenDao recoveryTokenDao) {
        this._encoder = passwordEncoder;
        this._userDao = userDao;
        _mail = mail;
        _recoveryTokeService = recoveryTokenDao;
    }

    public void changePassword(PasswordChangeRequest changeRequest, Principal principal) {
        var username = principal.getName();
        if(!changeRequest.getNuevaPass().equals(changeRequest.getConfirmarPass())){
            throw new BadRequestException("La nueva contraseña no coincide con su confirmación");
        }

        var user = getUser(username);

        if(!confirmPassword(changeRequest.getPassActual(), user.getPassword())){
            throw new BadRequestException("El valor de passActual es incorrecto");
        }

        user.setPassword(_encoder.encode(changeRequest.getNuevaPass()));
        _userDao.save(user);
    }

    public Boolean confirmPassword(String password, String hash) {
        return _encoder.matches(password, hash);
    }


    public void forgotPassword(String email){
        var user = getUser(email);

        var token = UUID.randomUUID().toString();

        var recoveryToken = PasswordRecoveryTokenBean.builder()
                .token(token)
                .usuario(user)
                .fechaCreacion(LocalDateTime.now())
                .fechaExpiracion(LocalDate.now().plusDays(_tokenDuration))
                .usado(false)
                .build();

        recoveryToken.isActive(true);

        _recoveryTokeService.save(recoveryToken);

        _mail.sendRecoveryPasswordEmail(user, token);
    }

    public void recoveryPassword(String token, String newPassword){
        var recoveryToken = _recoveryTokeService.findByTokenAndActiveIsTrue(token);

        if(recoveryToken.isEmpty()) throw new NotFoundException("Token no encontrado");

        if(!isTokenValid(recoveryToken.get())) {
            throw new BadRequestException("Token no válido");
        }

        var user = recoveryToken.get().getUsuario();
        user.setPassword(_encoder.encode(newPassword));
        _userDao.save(user);

        recoveryToken.get().setFechaUso(LocalDateTime.now());
        recoveryToken.get().setUsado(true);
        _recoveryTokeService.save(recoveryToken.get());
    }

    public Boolean verifyTokenValidation(String token){
        var tokenBean = _recoveryTokeService.findByTokenAndActiveIsTrue(token);
        if(tokenBean.isEmpty()) throw new NotFoundException("Token no encontrado");
        return isTokenValid(tokenBean.get());
    }

    private Boolean isTokenValid(PasswordRecoveryTokenBean recoveryToken){
        if(recoveryToken == null) return false;
        if(recoveryToken.getFechaExpiracion().isBefore(LocalDate.now())) throw new BadRequestException("El token ha expirado");
        if(recoveryToken.getUsado()) throw new BadRequestException("El token ya ha sido usado");

        return true;
    }

    private UsuarioBean getUser(String email){
        var user = _userDao.findByEmailAndActiveIsTrue(email);

        if(user.isEmpty()) throw new NotFoundException("Usuario no encontrado");

        return user.get();
    }

}
