package com.devs.powerfit.beans.programas;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente_programa")
    @NotNull(message = "El cliente programa no puede ser nulo")
    private ClienteProgramaBean clientePrograma;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_programa_item")
    @NotNull(message = "El programa item no puede ser nulo")
    private ProgramaItemBean programaItem;

    @NotNull(message = "El logrado no puede ser nulo")
    private Boolean logrado;
}
