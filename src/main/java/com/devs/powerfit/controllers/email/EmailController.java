package com.devs.powerfit.controllers.email;

import com.devs.powerfit.services.email.EmailService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para el envio de correos
 * Esto es para las pruebas, no deberia de estar en producción
 */
@Getter
@Setter
class MailRequest {
    private String body;
    private String subject;
}
@Getter
@Setter
class MailResponse {
    private String mensaje;
}

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    @Autowired
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/sendMail/{email}")
    public ResponseEntity<MailResponse> sendMail(@PathVariable String email) {
        MailRequest mailRequest = new MailRequest();
        mailRequest.setBody("Bienvenido a Powerfit.");
        mailRequest.setSubject("Titulo");
        emailService.sendEmail(email, mailRequest.getSubject(), mailRequest.getBody());

        MailResponse mailResponse = new MailResponse();
        mailResponse.setMensaje("Correo enviado");
        return new ResponseEntity<>(mailResponse, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/sendMail/{email}")
    public ResponseEntity<MailResponse> sendMail(@PathVariable String email, @RequestBody MailRequest request) {
        String body = request.getBody();
        String subject = request.getSubject();
        emailService.sendEmail(email, subject, body);

        MailResponse mailResponse = new MailResponse();
        mailResponse.setMensaje("Correo enviado");
        return new ResponseEntity<>(mailResponse, HttpStatus.OK);
    }

    //send to all morosos
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/sendMailMorosos")
    public ResponseEntity<MailResponse> sendMailMorosos() {
        emailService.sendEmailToMorosos();
        MailResponse mailResponse = new MailResponse();
        mailResponse.setMensaje("Correo enviado a morosos");
        return new ResponseEntity<>(mailResponse, HttpStatus.OK);
    }
}
