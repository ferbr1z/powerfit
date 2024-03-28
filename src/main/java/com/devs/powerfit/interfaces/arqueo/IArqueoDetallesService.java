package com.devs.powerfit.interfaces.arqueo;

import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.daos.movimientos.MovimientoDetalleDao;
import com.devs.powerfit.dtos.arqueo.ArqueoDetalleDto;
import com.devs.powerfit.interfaces.bases.IService;

import java.util.List;

public interface IArqueoDetallesService {
    public ArqueoDetalleDto crearArqueoDetalle(ArqueoDetalleDto arqueoDetalleDto);

    public List<ArqueoDetalleDto> getDetalles(Long id);
}
