package com.devs.powerfit.abstracts;

import com.devs.powerfit.interfaces.IDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class AbstractDto implements IDto {
    private Long id;

    @Override
    public Long getId(){
        return id;
    }

    @Override
    public void setId(Long id){
        this.id=id;
    }

}
