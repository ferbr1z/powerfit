package com.devs.powerfit.interfaces.bases;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.abstracts.AbstractDto;
import org.springframework.stereotype.Component;

@Component
public interface IMapper<B extends AbstractBean,D extends AbstractDto> {
    public B toBean(D dto);
    public D toDto(B bean);
}
