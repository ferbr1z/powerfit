package com.devs.powerfit.beans.cajas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "cajas")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaBean extends AbstractBean {
    @Column(unique = true)
    @NotNullAndNotBlank(message = "El nombre de la caja no puede ser nulo ni en blanco")
    private String nombre;
    @NotNull(message = "El monto no puede ser nulo")
    private Double monto;
    @Positive
    @NotNull(message = "El número de caja no puede ser nulo")
    private Long numeroCaja;
    private Long numeroFactura;
    private Long numeroTicket;
}
