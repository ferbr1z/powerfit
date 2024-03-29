package com.devs.powerfit.interfaces.arqueo;

import com.devs.powerfit.beans.arqueo.ArqueoBean;
import com.devs.powerfit.beans.arqueo.ArqueoDetalleBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.daos.movimientos.MovimientoDetalleDao;
import com.devs.powerfit.dtos.arqueo.ArqueoDetalleDto;
import com.devs.powerfit.interfaces.bases.IService;

import java.util.List;

public interface IArqueoDetallesService {
    public List<ArqueoDetalleBean> generarDetalles(ArqueoBean arqueo, List<MovimientoBean> movimientos);
    public void guardarDetalles(List<ArqueoDetalleBean> detalles);
}
