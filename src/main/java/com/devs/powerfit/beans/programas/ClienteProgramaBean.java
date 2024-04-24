package com.devs.powerfit.beans.programas;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@Table(name = "cliente_programa")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClienteProgramaBean extends AbstractBean {
    @ManyToOne
    @JoinColumn(name = "id_programa")
    private ProgramaBean programa;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private ClienteBean cliente;
    private String nombreCliente;
    private LocalTime fechaEvaluacion;
    @OneToMany(mappedBy = "clientePrograma")
    private List<ClienteProgramaItem> clienteProgramaItem;
}
