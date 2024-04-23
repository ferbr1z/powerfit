package com.devs.powerfit.controllers.email;

import com.devs.powerfit.services.email.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    private EmailService mailService;

    @GetMapping("/sendMail")
    public void sendMail() {
        mailService.sendEmail("{mail Address}", "Title", "Massage");
    }
}
