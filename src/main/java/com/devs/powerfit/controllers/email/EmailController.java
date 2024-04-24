package com.devs.powerfit.controllers.email;

import com.devs.powerfit.services.email.EmailService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para el envio de correos
 * Esto es para las pruebas, no deberia de estar en producción
 */
@Getter
class MailRequest {
    private String body;
    private String subject;
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
    public void sendMail(@PathVariable String email) {
        emailService.sendEmail(email, "Titulo", "Bienvenido a Powerfit.");
    }


    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PostMapping("/sendMail/{email}")
    public void sendMail(@PathVariable String email, @RequestBody MailRequest request) {
        String body = request.getBody();
        String subject = request.getSubject();
        System.out.println("Email: " + email);
        System.out.println("Body: " + body);
        System.out.println("Subject: " + subject);
        emailService.sendEmail(email, subject, body);
    }
}
