package com.devs.powerfit.services.facturas;

import com.devs.powerfit.dtos.facturas.FacturaDetalleDto;
import com.devs.powerfit.interfaces.facturas.IFacturaDetalleService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.stereotype.Service;

@Service
public class FacturaDetalleService implements IFacturaDetalleService {
    @Override
    public FacturaDetalleDto create(FacturaDetalleDto facturaDetalleDto) {
        return null;
    }

    @Override
    public FacturaDetalleDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<FacturaDetalleDto> getAll(int page) {
        return null;
    }

    @Override
    public FacturaDetalleDto update(Long id, FacturaDetalleDto facturaDetalleDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
}
