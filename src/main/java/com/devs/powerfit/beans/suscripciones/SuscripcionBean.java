package com.devs.powerfit.beans.suscripciones;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionBean extends AbstractBean {
    @OneToOne
    @JoinColumn(name = "id_cliente")
    private ClienteBean cliente;
    @Column
    private Double Total;

}
