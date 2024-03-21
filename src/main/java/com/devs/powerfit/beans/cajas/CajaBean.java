package com.devs.powerfit.beans.cajas;

import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "cajas")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CajaBean extends AbstractBean {
    @Column(unique = true)
    private String nombre;
    private Double monto;
}
