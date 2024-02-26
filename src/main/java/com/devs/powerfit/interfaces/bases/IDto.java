package com.devs.powerfit.interfaces.bases;

import org.springframework.stereotype.Component;

import java.io.Serializable;

public interface IDto extends Serializable {
    public Long getId();
    public void setId(Long id);
}

