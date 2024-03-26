package com.devs.powerfit.services.movimientos;

import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.dtos.facturas.FacturaProveedorDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDto;
import com.devs.powerfit.interfaces.movimientos.IMovimientoService;
import com.devs.powerfit.utils.responses.PageResponse;

import java.util.Date;
import java.util.List;

public class MovimientoService implements IMovimientoService {
    @Override
    public MovimientoDto create(MovimientoDto movimientoDto) {
        return null;
    }

    @Override
    public MovimientoDto getById(Long id) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> getAll(int page) {
        return null;
    }

    @Override
    public MovimientoDto update(Long id, MovimientoDto movimientoDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public PageResponse<MovimientoDto> searchBySesionId(Long sesionId, int page) {
        return null;
    }

    @Override
    public List<MovimientoDto> getAllBySesionId(Long sesionId) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> searchBetweenFecha(int page, Date fechaMenor, Date fechaMayor) {
        return null;
    }

    @Override
    public List<MovimientoDto> getAllBetweenFecha(Date fechaMenor, Date fechaMayor) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> searchBySesion(int page, Date fechaMenor, Date fechaMayor) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> searchBySesionAndEntrada(int page, Long sesionId, boolean entrada) {
        return null;
    }

    @Override
    public List<MovimientoDto> getAllBySesionAndEntrada(Long sesionId, boolean entrada) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> searchByFactura(int page, FacturaDto factura) {
        return null;
    }

    @Override
    public List<MovimientoDto> getAllByFactura(FacturaDto factura) {
        return null;
    }

    @Override
    public PageResponse<MovimientoDto> searchByFacturaProveedor(int page, FacturaProveedorDto facturaProveedor) {
        return null;
    }

    @Override
    public List<MovimientoDto> getAllByFacturaProveedor(FacturaProveedorDto facturaProveedor) {
        return null;
    }
}
