package com.devs.powerfit.beans.programas;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "cliente_programa_items")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteProgramaItem extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "id_cliente_programa")
    private ClienteProgramaBean clientePrograma;
    @OneToOne
    @JoinColumn(name = "id_programa_item")
    private ProgramaItemBean programaItem;
    private String tiempo;
    private Integer repeticiones;
    private Double peso;
    private Boolean logrado;
}
