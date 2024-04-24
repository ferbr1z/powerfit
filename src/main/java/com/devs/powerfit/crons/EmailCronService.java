
package com.devs.powerfit.crons;

import com.devs.powerfit.services.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Service
public class EmailCronService {

    private final EmailService emailService;
    @Autowired
    public EmailCronService(EmailService emailService) {
        this.emailService = emailService;
    }

    //0 0 12 ? * MON * = Se ejecuta todos los lunes a las 12:00 PM
    // cada minuto = "0 0/1 * 1/1 * ? *"
    @Scheduled(cron = "0 * * * * *")
    public void enviarCorreoAMorosos()  {
        emailService.sendEmailToMorosos();
    }
}
