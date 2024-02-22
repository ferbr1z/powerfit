package com.devs.powerfit.interfaces;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.dtos.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IService<T extends AbstractDto> {
    public T create(T t);
    public T getById(Long id);
    public PageResponse<T> getAll(int pag);
    public T update(Long id, T t);
    public boolean delete(Long id);
}
