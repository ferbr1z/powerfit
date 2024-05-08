package com.devs.powerfit.abstracts;

import com.devs.powerfit.interfaces.bases.IDto;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractDto implements IDto {
    private Long id;
    @Getter
    @Column(columnDefinition = "boolean")
    @ColumnDefault("true")
    private boolean active;

    @Override
    public Long getId(){
        return id;
    }

    @Override
    public void setId(Long id){
        this.id=id;
    }

}
