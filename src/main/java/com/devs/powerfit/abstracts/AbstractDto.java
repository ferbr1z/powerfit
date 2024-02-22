package com.devs.powerfit.abstracts;

import com.devs.powerfit.interfaces.IDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
