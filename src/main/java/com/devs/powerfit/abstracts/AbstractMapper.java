package com.devs.powerfit.abstracts;

import com.devs.powerfit.abstracts.AbstractBean;
import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.interfaces.bases.IMapper;
import org.modelmapper.ModelMapper;
public abstract class AbstractMapper<T extends AbstractBean, K extends AbstractDto> implements IMapper<T, K> {
    protected ModelMapper modelMapper = new ModelMapper();
}
