package com.devs.powerfit.beans.programas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.utils.anotaciones.NotNullAndNotBlank;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Table(name = "programas")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaBean extends AbstractBean {
    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_actividad")
    private ActividadBean actividad;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "id_entrenador")
    private EmpleadoBean empleado;

    @NotNull
    @NotBlank
    private String titulo;

    @NotNull
    private ENivelPrograma nivel;

    private ESexo sexo;

    @OneToMany(mappedBy = "programa")
    private List<ProgramaItemBean> items;

    @OneToMany(mappedBy = "programa")
    private List<ClienteProgramaBean> clienteProgramas;

}
