package com.devs.powerfit.services.cajas;

import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.interfaces.cajas.ICajaService;
import com.devs.powerfit.utils.responses.PageResponse;

public class CajaService implements ICajaService {
    @Override
    public CajaDto create(CajaDto cajaDto) {
        return null;
    }

    @Override
    public CajaDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<CajaDto> getAll(int page) {
        return null;
    }

    @Override
    public CajaDto update(Long id, CajaDto cajaDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
