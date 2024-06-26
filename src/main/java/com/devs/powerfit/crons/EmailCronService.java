
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

    //0 0 12 * * MON = Se ejecuta todos los lunes a las 12:00 PM
    // cada minuto = "0 * * * * *"
    @Scheduled(cron = "0 0 10 * * MON")
    public void enviarCorreoAMorosos()  {
        emailService.sendEmailToMorosos();
    }

    //cada mes el dia 14 a las 9:00 AM
    //@Scheduled(cron="0 0 9 25 * ?")
    @Scheduled(cron = "0 0 9 14 * ?")
    public void enviarCorreoReportes()  { emailService.sendReportesEmail(); }
}
