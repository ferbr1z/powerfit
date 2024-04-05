package com.devs.powerfit.beans.mediciones;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Table(name = "mediciones")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicionBean extends AbstractBean {
    @Column
    @NotNull(message = "Fecha no puede ser null")
    private Date fecha;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteBean cliente;
    @Column
    @Positive(message = "El peso debe ser positivo en kilogramos")
    @NotNull(message = "El peso no puede ser null")
    private Double peso;
    @Column
    @Positive(message = "La altura debe ser positiva en metros")
    @NotNull(message = "La altura no puede ser null")
    private Double altura;
    @Column
    @Positive(message = "El IMC debe ser positivo ")
    @NotNull(message = "El IMC no puede ser null")
    private Double imc;
    @Column
    @Positive(message = "La Circunferencia de brazo  debe ser positiva en centímetros")
    private Double cirBrazo;
    @Column
    @Positive(message = "La Circunferencia de piernas debe ser positiva en centímetros")
    private Double cirPiernas;
    @Column
    @Positive(message = "La Circunferencia de cintura  debe ser positiva en centímetros")
    private Double cirCintura;
    @Column
    @Positive(message = "La Circunferencia de pecho  debe ser positiva en centímetros")
    private Double cirPecho;
}
