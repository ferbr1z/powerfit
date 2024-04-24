package com.devs.powerfit.beans.tipoDePagos;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tipos_de_pago")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoDePagoBean extends AbstractBean {
    @NotNullAndNotBlank(message = "El nombre no puede ser nulo ni estar en blanco")
    @Size(max = 50,message = "El máximo de caracteres es 50")
    @Column(unique = true)
    private String nombre;
    @NotNullAndNotBlank(message = "La descripción no puede ser nula ni estar en blanco")
    @Size(max = 200,message = "El máximo de caracteres es 200")
    private String descripcion;
}
