
package com.devs.powerfit.crons;

import com.devs.powerfit.services.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class EmailCronService {

    private final EmailService emailService;
    @Autowired
    public EmailCronService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 0 * * 0")
    public void enviarCorreoAMorosos() {
        emailService.sendEmailToMorosos();
        //En progreso...
    }
}
