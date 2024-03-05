package com.devs.powerfit.beans.mediciones;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import jakarta.persistence.*;
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
    private Date fecha;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteBean cliente;
    @Column
    private Double peso;
    @Column
    private Double altura;
    @Column
    private Double imc;
    @Column
    private Double cirBrazo;
    @Column
    private Double cirPiernas;
    @Column
    private Double cirCintura;
    @Column
    private Double cirPecho;
}
