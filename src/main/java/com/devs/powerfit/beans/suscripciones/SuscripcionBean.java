package com.devs.powerfit.beans.suscripciones;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.enums.EModalidad;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuscripcionBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @NotNull(message = "Cliente no puede ser null")
    private ClienteBean cliente;
    @ManyToOne
    @JoinColumn(name = "id_actividad")
    @NotNull(message = "Actividad no puede ser null")
    private ActividadBean actividad;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Estado no puede ser null")
    private EEstado estado;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Modalidad no puede ser null")
    private EModalidad modalidad;
    @Column
    private Double monto;
    private LocalDate fechaInicio;
    @Column
    LocalDate fechaFin;
    boolean finalizado;
}
