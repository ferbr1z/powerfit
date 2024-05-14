package com.devs.powerfit.beans.email;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name = "email_report")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailReportBean extends AbstractBean {
    @Column
    private String reportEmail;
}