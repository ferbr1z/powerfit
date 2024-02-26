package com.devs.powerfit.interfaces.bases;

import com.devs.powerfit.abstracts.AbstractDto;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IService<T extends AbstractDto> {
    public T create(T t);
    public T getById(Long id);
    public PageResponse<T> getAll(int page);
    public T update(Long id, T t);
    public boolean delete(Long id);
}
