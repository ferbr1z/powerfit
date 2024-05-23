package com.devs.powerfit.services.auth;

import com.devs.powerfit.beans.auth.PasswordRecoveryTokenBean;
import com.devs.powerfit.beans.auth.RolBean;
import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.daos.auth.PasswordRecoveryTokenDao;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import com.devs.powerfit.interfaces.empleados.IEmpleadoService;
import com.devs.powerfit.security.password.PasswordChangeRequest;
import com.devs.powerfit.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PasswordService {

    private PasswordEncoder _encoder;
    private UsuarioDao _userDao;
    private PasswordRecoveryTokenDao _recoveryTokeService;
    private EmailService _mail;
    private IClienteService _clienteService;
    private IEmpleadoService _empleadoService;

    @Value("${app.password.token.duration}")
    private Integer _tokenDuration;

    @Autowired
    public PasswordService(PasswordEncoder passwordEncoder, UsuarioDao userDao, EmailService mail, PasswordRecoveryTokenDao recoveryTokenDao, IClienteService clienteService, IEmpleadoService empleadoService) {
        this._encoder = passwordEncoder;
        this._userDao = userDao;
        _mail = mail;
        _recoveryTokeService = recoveryTokenDao;
        _clienteService = clienteService;
        _empleadoService = empleadoService;
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

        validatePassword(changeRequest.getNuevaPass(), user);

        user.setPassword(_encoder.encode(changeRequest.getNuevaPass()));
        _userDao.save(user);
    }

    public void forgotPassword(String email){
        var user = getUser(email);

        var token = generateToken();

        var recoveryToken = PasswordRecoveryTokenBean.builder()
                .token(token)
                .usuario(user)
                .fechaCreacion(LocalDateTime.now())
                .fechaExpiracion(LocalDate.now().plusDays(_tokenDuration))
                .usado(false)
                .build();

        recoveryToken.setActive(true);

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

    public Boolean needChange(Principal principal){
        var user = getUser(principal.getName());
        if(user.getRol().getId() == 2) {
            var cliente = _clienteService.getByEmail(principal.getName());
            return confirmPassword(cliente.getCedula(), user.getPassword());
        }

        var empleado = _empleadoService.getByEmail(principal.getName());
        return confirmPassword(empleado.getCedula(), user.getPassword());
    }

    private String generateToken(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private Boolean confirmPassword(String password, String hash) {
        return _encoder.matches(password, hash);
    }

    /**
     * Valida que la contraseña cumpla con los requisitos mínimos
     * @param password
     * @param user
     */
    private void validatePassword(String password, UsuarioBean user){
        if(password.length() < 6) throw new BadRequestException("La contraseña debe tener al menos 6 caracteres");

        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);

        if(!matcher.matches())
            throw new BadRequestException("La contraseña debe tener al menos una letra mayúscula, una letra minúscula, un número y un caracter especial");
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
