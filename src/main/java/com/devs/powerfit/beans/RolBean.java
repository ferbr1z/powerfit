package com.devs.powerfit.beans;
import com.devs.powerfit.abstracts.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "roles")
public class RolBean extends AbstractBean {
    @Column
    private String nombre;

    @OneToMany(mappedBy = "rol")
    private List<UsuarioBean> usuarios;

}
