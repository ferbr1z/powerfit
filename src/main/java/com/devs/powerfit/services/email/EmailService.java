package com.devs.powerfit.services.email;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.utils.Setting;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;

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
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            String htmlContent = templateEngine.process(templateName, context);
            helper.setText(htmlContent, true);

            FileSystemResource res = new FileSystemResource(new File("src/main/resources/images/pwflogo1.png"));
            helper.addInline("pwflogo1", res);
            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    //Enviar correo a todos los morosos
    public void sendEmailToMorosos() {
        var morosos = clienteDao.findClientsWithPendingSubscriptions();
        for (ClienteBean moroso : morosos) {
            //TODO: mostrar todas las páginas
            var pag = PageRequest.of(0, Setting.PAGE_SIZE);
            var suscripcionDetalles = suscripcionDao.findAllByClienteIdAndEstadoAndActiveTrue(pag, moroso.getId(), EEstado.PENDIENTE);

            Context context = new Context();
            String year = String.valueOf(java.time.LocalDate.now().getYear());
            context.setVariable("year", year);
            context.setVariable("subscriptions", suscripcionDetalles.getContent());
            context.setVariable("nombre", moroso.getNombre());
            String subject = "Powerfit: Recordatorio de pago";

            sendEmailWithHtmlTemplate(moroso.getEmail(), subject, "email-template", context);
        }
    }

    public void sendRecoveryPasswordEmail(UsuarioBean user) {
        Context context = new Context();

        String year = String.valueOf(java.time.LocalDate.now().getYear());
        context.setVariable("user", user);
        context.setVariable("year", year);
        String subject = "Powerfit: Restablecer contraseña";
        sendEmailWithHtmlTemplate(user.getEmail(), subject, "forgot-password-template", context);
    }

}