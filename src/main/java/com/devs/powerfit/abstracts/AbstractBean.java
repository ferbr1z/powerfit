package com.devs.powerfit.abstracts;

import com.devs.powerfit.interfaces.bases.IBean;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

@MappedSuperclass
@Data

public abstract class AbstractBean implements IBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Getter
    @Column(columnDefinition = "boolean")
    @ColumnDefault("true")
    private boolean active;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}