package com.devs.powerfit.services.arqueo;

import com.devs.powerfit.beans.arqueo.ArqueoBean;
import com.devs.powerfit.beans.arqueo.ArqueoDetalleBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.beans.movimientos.MovimientoDetalleBean;
import com.devs.powerfit.daos.arqueo.ArqueoDao;
import com.devs.powerfit.daos.arqueo.ArqueoDetalleDao;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.daos.movimientos.MovimientoDao;
import com.devs.powerfit.daos.movimientos.MovimientoDetalleDao;
import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import com.devs.powerfit.dtos.arqueo.ArqueoDetalleDto;
import com.devs.powerfit.utils.mappers.arqueoMapper.ArqueoDetalleMapper;
import com.devs.powerfit.utils.mappers.arqueoMapper.ArqueoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArqueoService {

    private final ArqueoDao arqueoDao;
    private final ArqueoDetalleDao arqueoDetalleDao;
    private final MovimientoDao movimientoDao;

    private final SesionCajaDao sesionCajaDao;

    private final MovimientoDetalleDao movimientoDetalleDao;

    private final ArqueoMapper mapper;

    private final ArqueoDetalleMapper detalleMapper;

    @Autowired
    public ArqueoService(ArqueoDao arqueoDao, ArqueoDetalleDao arqueoDetalleDao, MovimientoDao movimientoDao, SesionCajaDao sesionCajaDao, MovimientoDetalleDao movimientoDetalleDao, ArqueoMapper mapper, ArqueoDetalleMapper detalleMapper) {
        this.arqueoDao = arqueoDao;
        this.arqueoDetalleDao = arqueoDetalleDao;
        this.movimientoDao = movimientoDao;
        this.sesionCajaDao = sesionCajaDao;
        this.movimientoDetalleDao = movimientoDetalleDao;
        this.mapper = mapper;
        this.detalleMapper = detalleMapper;
    }

    public ArqueoDto realizarArqueo(Long sesionCajaId) {
        // Buscar la sesión de caja por su ID
        Optional<SesionCajaBean> sesionCajaOptional = sesionCajaDao.findByIdAndActiveTrue(sesionCajaId);
        if (!sesionCajaOptional.isPresent()) {
            throw new RuntimeException("La sesión de caja con el ID " + sesionCajaId + " no existe.");
        }
        SesionCajaBean sesionCaja = sesionCajaOptional.get();

        // Crear un nuevo arqueo
        ArqueoBean arqueo = new ArqueoBean();
        arqueo.setActive(true);
        arqueo.setSesionCaja(sesionCaja);
        arqueo.setFecha(new Date());
        arqueo.setHora(new Date());
        arqueo.setMontoTotal(0.0); // Inicializar el monto total

        // Obtener todos los movimientos asociados a la sesión de caja
        List<MovimientoBean> movimientos = movimientoDao.findAllBySesionAndActiveTrue(sesionCaja);

        // Iterar sobre los movimientos para calcular el monto total y generar los detalles del arqueo
        List<ArqueoDetalleBean> detalles = new ArrayList<>();
        for (MovimientoBean movimiento : movimientos) {
            // Obtener los detalles de movimiento asociados al movimiento
            List<MovimientoDetalleBean> detallesMovimiento = movimientoDetalleDao.findAllByMovimientoAndActiveTrue(movimiento);
            for (MovimientoDetalleBean detalleMovimiento : detallesMovimiento) {
                // Crear un detalle por cada monto de entrada
                if (detalleMovimiento.getMonto() > 0) {
                    ArqueoDetalleBean detalleEntrada = new ArqueoDetalleBean();
                    detalleEntrada.setActive(true);
                    detalleEntrada.setArqueo(arqueo);
                    detalleEntrada.setMontoEntrada(detalleMovimiento.getMonto());
                    detalleEntrada.setMontoSalida(0.0);
                    detalleEntrada.setMovimiento(movimiento);
                    detalles.add(detalleEntrada);
                } else {
                    // Crear un detalle por cada monto de salida
                    ArqueoDetalleBean detalleSalida = new ArqueoDetalleBean();
                    detalleSalida.setActive(true);
                    detalleSalida.setArqueo(arqueo);
                    detalleSalida.setMontoEntrada(0.0);
                    detalleSalida.setMontoSalida(Math.abs(detalleMovimiento.getMonto()));
                    detalleSalida.setMovimiento(movimiento);
                    detalles.add(detalleSalida);
                }

                // Sumar el monto total del movimiento al arqueo
                arqueo.setMontoTotal(arqueo.getMontoTotal() - detalleMovimiento.getMonto());
            }
        }

        // Guardar el arqueo y sus detalles en la base de datos
        arqueo.setDetalles(detalles);
        arqueoDao.save(arqueo);

        // Crear DTOs para la cabecera del arqueo y sus detalles
        ArqueoDto arqueoDto = new ArqueoDto();
        arqueoDto = mapper.toDto(arqueo);
        arqueoDto.setSesionCajaId(arqueo.getSesionCaja().getId());


        List<ArqueoDetalleDto> detallesDto = detalles.stream()
                .map(detalleMapper::toDto)
                .collect(Collectors.toList());
        arqueoDto.setDetalles(detallesDto);

        return arqueoDto;
    }

}