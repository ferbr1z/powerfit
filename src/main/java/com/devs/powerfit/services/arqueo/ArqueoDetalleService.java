package com.devs.powerfit.services.arqueo;

import com.devs.powerfit.beans.arqueo.ArqueoBean;
import com.devs.powerfit.beans.arqueo.ArqueoDetalleBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.beans.movimientos.MovimientoDetalleBean;
import com.devs.powerfit.daos.arqueo.ArqueoDetalleDao;
import com.devs.powerfit.daos.movimientos.MovimientoDetalleDao;
import com.devs.powerfit.interfaces.arqueo.IArqueoDetallesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArqueoDetalleService implements IArqueoDetallesService {

    private ArqueoDetalleDao arqueoDetalleDao;

    private final MovimientoDetalleDao movimientoDetalleDao;

    @Autowired
    public ArqueoDetalleService(ArqueoDetalleDao arqueoDetalleDao,MovimientoDetalleDao movimientoDetalleDao) {
        this.arqueoDetalleDao = arqueoDetalleDao;
        this.movimientoDetalleDao = movimientoDetalleDao;
    }



    @Override
    public List<ArqueoDetalleBean> generarDetalles(ArqueoBean arqueo, List<MovimientoBean> movimientos) {
        return movimientos.stream()
                .flatMap(movimiento -> {
                    List<MovimientoDetalleBean> detallesMovimiento = movimientoDetalleDao.findAllByMovimientoAndActiveTrue(movimiento);
                    return detallesMovimiento.stream()
                            .map(detalleMovimiento -> {
                                ArqueoDetalleBean detalle = new ArqueoDetalleBean();
                                detalle.setActive(true);
                                detalle.setArqueo(arqueo);
                                detalle.setMontoEntrada(movimiento.isEntrada() ? detalleMovimiento.getMonto() : 0.0);
                                detalle.setMontoSalida(movimiento.isEntrada() ? 0.0 : detalleMovimiento.getMonto());
                                detalle.setMovimiento(movimiento);
                                return detalle;
                            });
                })
                .collect(Collectors.toList());
    }



    @Override
    public void guardarDetalles(List<ArqueoDetalleBean> detalles) {
        for (ArqueoDetalleBean detalle : detalles) {
            detalle.setActive(true); // Asegurarse de que los detalles estén activos antes de guardarlos
        }
        arqueoDetalleDao.saveAll(detalles);
    }



}
