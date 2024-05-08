package com.devs.powerfit.services.auth;

import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.security.password.PasswordChangeRequest;
import com.devs.powerfit.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class PasswordService {

    private PasswordEncoder _encoder;
    private UsuarioDao _userDao;
    private EmailService _mail;

    @Autowired
    public PasswordService(PasswordEncoder passwordEncoder, UsuarioDao userDao, EmailService mail) {
        this._encoder = passwordEncoder;
        this._userDao = userDao;
        _mail = mail;
    }

    public void changePassword(PasswordChangeRequest changeRequest, Principal principal) {
        var username = principal.getName();
        if(!changeRequest.getNuevaPass().equals(changeRequest.getConfirmarPass())){
            throw new BadRequestException("La nueva contraseña no coincide con su confirmación");
        }
        var user = _userDao.findByEmailAndActiveIsTrue(username);

        if(user.isEmpty()) throw new NotFoundException("Usuario no encontrado");

        if(!confirmPassword(changeRequest.getPassActual(), user.get().getPassword())){
            throw new BadRequestException("El valor de passActual es incorrecto");
        }

        user.get().setPassword(_encoder.encode(changeRequest.getNuevaPass()));
        _userDao.save(user.get());
    }

    public Boolean confirmPassword(String password, String hash) {
        return _encoder.matches(password, hash);
    }


    public void forgotPassword(String email){
        var user = _userDao.findByEmailAndActiveIsTrue(email);

        if(user.isEmpty()) throw new NotFoundException("Usuario no encontrado");

        _mail.sendRecoveryPasswordEmail(user.get());

    }


}
