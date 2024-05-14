package com.devs.powerfit.beans.programas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "cliente_programa", uniqueConstraints = {
        // Sólo puede haber un cliente por programa de entrenamiento
        @UniqueConstraint(columnNames = {"id_programa", "id_cliente"})})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteProgramaBean extends AbstractBean {
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_programa")
    @NotNull(message = "El programa no puede ser nulo")
    private ProgramaBean programa;

    private Double porcentaje;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente")
    @NotNull(message = "El cliente no puede ser nulo")
    private ClienteBean cliente;

    @NotNull(message = "La fecha de evaluación no puede ser nula")
    private LocalDate fechaEvaluacion;

    @OneToMany(mappedBy = "clientePrograma", fetch = FetchType.EAGER)
    private List<ClienteProgramaItem> clienteProgramaItem;
}
