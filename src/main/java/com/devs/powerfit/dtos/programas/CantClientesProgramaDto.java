package com.devs.powerfit.dtos.programas;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CantClientesProgramaDto extends AbstractDto {
    private String titulo;
    private ENivelPrograma nivel;
    private ESexo sexo;
    private String nombreActividad;
    private String nombreEmpleado;
    private Long cantClientes; // long porque el orm lo usa

    // Este constructor es necesario para listar los datos. ProgramaDao no funciona sin este constructor
    public CantClientesProgramaDto(Long id, boolean active, String titulo, ENivelPrograma nivel, ESexo sexo, String nombreActividad, String nombreEmpleado, Long cantClientes) {
        super(id, active);
        this.titulo = titulo;
        this.nivel = nivel;
        this.sexo = sexo;
        this.nombreActividad = nombreActividad;
        this.nombreEmpleado = nombreEmpleado;
        this.cantClientes = cantClientes;
    }


}
