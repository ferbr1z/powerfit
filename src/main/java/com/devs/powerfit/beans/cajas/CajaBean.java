package com.devs.powerfit.beans.cajas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "cajas")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaBean extends AbstractBean {
    @Column(unique = true)
    @NotNullAndNotBlank
    private String nombre;
    private Double monto;
    @Positive
    private Long numeroCaja;
    private Long numeroFactura;
}
