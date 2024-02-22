package com.devs.powerfit.abstracts;

import com.devs.powerfit.interfaces.IBean;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@MappedSuperclass
public abstract class AbstractBean implements IBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "boolean")
    @ColumnDefault("true")
    private boolean active;

    @Override
    public Long getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}