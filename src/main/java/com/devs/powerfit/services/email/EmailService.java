package com.devs.powerfit.services.email;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.enums.EEstado;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service(value= "MailService")
@Transactional
public class EmailService {


    private JavaMailSender emailSender;
    private final ClienteDao clienteDao;
    private final SuscripcionDao suscripcionDao;
    private final TemplateEngine templateEngine;
    @Autowired
    public EmailService(ClienteDao clienteDao, SuscripcionDao suscripcionDao, JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
        this.clienteDao = clienteDao;
        this.suscripcionDao = suscripcionDao;
    }

    //validacion de email
    public boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
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

    public void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    //Enviar correo de bienvenida
    public void sendWelcomeEmail(String to) throws MessagingException {
        String subject = "Bienvenido a Powerfit";
        String body = "Bienvenido a Powerfit, esperamos que disfrutes de nuestros servicios.";
        sendEmail(to, subject, body);
    }

    //Enviar correo a todos los morosos
    public void sendEmailToMorosos() {
        var morosos = clienteDao.findClientsWithPendingSubscriptions();
        for (ClienteBean moroso : morosos) {
            //get pageable of this Page<SuscripcionBean>findAllByClienteIdAndEstadoAndActiveTrue(Pageable pageable,Long id,  EEstado estado);
            var suscripciones = suscripcionDao.findAllByClienteIdAndEstadoAndActiveTrue(moroso.getId(), EEstado.PENDIENTE);
            Context context = new Context();
            context.setVariable("message", moroso.getNombre());
            String subject = "Powerfit: Recordatorio de pago";

            sendEmailWithHtmlTemplate(moroso.getEmail(), subject, "email-template", context);
        }
    }

}