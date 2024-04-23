package com.devs.powerfit.beans.programas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import jakarta.persistence.*;
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
    @JoinColumn(name = "id_actividad")
    private ActividadBean actividad;
    @OneToOne
    @JoinColumn(name = "id_entrenador")
    private EmpleadoBean entrenador;
    private String titulo;
    private ENivelPrograma nivel;
    private ESexo sexo;
    @OneToMany(mappedBy = "programa")
    private List<ProgramaItemBean> items;
}
