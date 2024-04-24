package com.devs.powerfit.beans.programas;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "programa_items")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramaItemBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "id_programa")
    private ProgramaBean programa;
    private String nombre;
    private String descripcion;
    private String tiempo;
    private Double peso;
    private Integer repeticiones;
}
