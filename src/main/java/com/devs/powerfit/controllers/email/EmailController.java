package com.devs.powerfit.controllers.email;

import com.devs.powerfit.interfaces.cajas.ICajaService;
import com.devs.powerfit.interfaces.cajas.ISesionCajaService;
import com.devs.powerfit.services.cajas.ReporteCajaService;
import com.devs.powerfit.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        emailService.sendEmail(email, "Title", "Massage");
    }
}
