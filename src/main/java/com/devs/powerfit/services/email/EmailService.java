package com.devs.powerfit.services.email;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.daos.clientes.ClienteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service(value= "MailService")
@Transactional
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    private final ClienteDao clienteDao;

    @Autowired
    public EmailService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    //validacion de email
    public boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public void sendEmail(String to, String subject, String body) {
        if (!validateEmail(to)) {
            throw new IllegalArgumentException("Correo inválido");
        } else if (subject == null || subject.isEmpty()) {
            throw new IllegalArgumentException("Subject inválido");
        } else if (body == null || body.isEmpty()) {
            throw new IllegalArgumentException("Body inválido");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        emailSender.send(message);
    }

    //Enviar correo de bienvenida
    public void sendWelcomeEmail(String to) {
        String subject = "Bienvenido a Powerfit";
        String body = "Bienvenido a Powerfit, esperamos que disfrutes de nuestros servicios.";
        sendEmail(to, subject, body);
    }

    //Enviar correo a todos los morosos
    public void sendEmailToMorosos() {
        var morosos = clienteDao.findClientsWithPendingSubscriptions();
        //String[] morososEmails = new String[] {};
        for (ClienteBean moroso : morosos) {
            String subject = "Recordatorio de pago";
            String body = "Estimado " + moroso.getNombre() + ", le recordamos que tiene una suscripción pendiente.";
            sendEmail(moroso.getEmail(), subject, body);
        }
    }

}