package com.devs.powerfit.beans.tipoDePagos;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
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
    @NotNullAndNotBlank
    @Size(max = 50)
    private String nombre;
    @NotNullAndNotBlank
    @Size(max = 200)
    private String descripcion;
}
