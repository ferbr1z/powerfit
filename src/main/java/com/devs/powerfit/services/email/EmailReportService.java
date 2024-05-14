package com.devs.powerfit.services.email;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.email.EmailReportBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.daos.email.EmailReportDao;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.email.EmailReportDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionGananciasDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionesEstadisticasDto;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.services.clientes.ClienteListaService;
import com.devs.powerfit.services.clientes.ReportesClienteService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailReportService {

    private EmailReportDao emailReportDao;
    @Autowired
    public EmailReportService(EmailReportDao emailReportDao) {
        this.emailReportDao = emailReportDao;
    }

    public EmailReportDto get() {
        Optional<EmailReportBean> emailReport = emailReportDao.findFirstByOrderByIdAsc();
        if (emailReport.isPresent()) {
            EmailReportDto emailReportDto = new EmailReportDto();
            emailReportDto.setActive(true);
            emailReportDto.setId(emailReport.get().getId());
            emailReportDto.setReportEmail(emailReport.get().getReportEmail());
            return emailReportDto;
        } else {
            return null;
        }
    }

    public EmailReportDto create() {
        Optional<EmailReportBean> emailReport = emailReportDao.findFirstByOrderByIdAsc();
        if (emailReport.isEmpty()) {
            EmailReportBean emailReportBean = new EmailReportBean();
            emailReportBean.setActive(true);
            emailReportDao.save(emailReportBean);

            EmailReportDto emailReportDto = new EmailReportDto();
            emailReportDto.setActive(true);
            emailReportDto.setId(emailReportBean.getId());
            emailReportDto.setReportEmail(emailReportBean.getReportEmail());
            return emailReportDto;
        } else {
            return null;
        }
    }

    public EmailReportDto update(EmailReportDto emailreportDto) {
        //si el email es null, poner en la database null
        if (emailreportDto.getReportEmail() == null) {
            emailreportDto.setReportEmail(null);
        } else if (!validateEmail(emailreportDto.getReportEmail())) {
            throw new IllegalArgumentException("Correo inválido, verifica el formato.");
        }
        var emailReport = emailReportDao.findFirstByOrderByIdAsc();
        if (emailReport.isPresent()) {
            EmailReportBean emailReportBean = emailReport.get();
            emailReportBean.setActive(true);
            emailReportBean.setReportEmail(emailreportDto.getReportEmail());
            emailReportDao.save(emailReportBean);
            return emailreportDto;
        } else {
            return null;
        }
    }


    //validacion de email
    public boolean validateEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    }
}


