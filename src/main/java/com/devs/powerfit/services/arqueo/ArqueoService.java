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
import com.devs.powerfit.dtos.arqueo.ArqueoDetalleDto;
import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import com.devs.powerfit.dtos.arqueo.ArqueoRequestDto;
import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.arqueo.IArqueoService;
import com.devs.powerfit.interfaces.cajas.ICajaService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.arqueoMapper.ArqueoDetalleMapper;
import com.devs.powerfit.utils.mappers.arqueoMapper.ArqueoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArqueoService implements IArqueoService {

    private final ArqueoDao arqueoDao;
    private final ArqueoDetalleDao arqueoDetalleDao;
    private final MovimientoDao movimientoDao;
    private final SesionCajaDao sesionCajaDao;
    private final ArqueoMapper mapper;
    private final ArqueoDetalleMapper detalleMapper;
    private final ArqueoDetalleService detalleService;
    private final MovimientoDetalleDao movimientoDetalleDao;
    private final ICajaService cajaService;
    @Autowired
    public ArqueoService(ArqueoDao arqueoDao, ArqueoDetalleDao arqueoDetalleDao, MovimientoDao movimientoDao, SesionCajaDao sesionCajaDao, ArqueoMapper mapper, ArqueoDetalleMapper detalleMapper, ArqueoDetalleService detalleService, MovimientoDetalleDao movimientoDetalleDao, ICajaService cajaService) {
        this.arqueoDao = arqueoDao;
        this.arqueoDetalleDao = arqueoDetalleDao;
        this.movimientoDao = movimientoDao;
        this.sesionCajaDao = sesionCajaDao;
        this.mapper = mapper;
        this.detalleMapper = detalleMapper;
        this.detalleService = detalleService;
        this.movimientoDetalleDao = movimientoDetalleDao;
        this.cajaService = cajaService;
    }


    @Override
    public ArqueoDto realizarArqueo(ArqueoRequestDto arqueoRequestDto) {
        //crear y guardar el arqueo
        SesionCajaBean sesionCaja = obtenerSesionCaja(arqueoRequestDto.getSesionCajaId());
        CajaDto caja = cajaService.getById(arqueoRequestDto.getCajaId());
        ArqueoBean arqueo = crearArqueo(sesionCaja);
        List<MovimientoBean> movimientos = obtenerMovimientos(sesionCaja);
        List<ArqueoDetalleBean> detalles = detalleService.generarDetalles(arqueo, movimientos);

        arqueo.setNombreCaja(caja.getNombre());
        // Calcular los totales de entrada y salida de cada tipo de pago
        calcularTotales(arqueo, detalles);

        // Calcular el monto total del arqueo
        double montoTotal = calcularMontoTotal(detalles);
        arqueo.setMontoTotal(arqueoRequestDto.getMontoApertura());
        arqueo.setMontoApertura(arqueoRequestDto.getMontoApertura() - montoTotal);

        guardarDetalles(detalles);
        arqueo.setDetalles(detalles);
        arqueoDao.save(arqueo);
        return crearArqueoDto(arqueo);
    }

    @Override
    public PageResponse<ArqueoDto> getAll(int page) {
        PageRequest pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<ArqueoBean> arqueos = arqueoDao.findAllByActiveIsTrue(pag);
        if (arqueos.isEmpty()){
            throw new NotFoundException("No hay arqueos en la lista");
        }
        Page<ArqueoDto> arqueosDto = arqueos.map(arqueo -> {
            ArqueoDto arqueoDto = mapper.toDto(arqueo);
            List<ArqueoDetalleBean> detalles = arqueoDetalleDao.findByArqueo_IdAndActiveIsTrue(arqueo.getId());
            List<ArqueoDetalleDto> detallesDto = detalles.stream()
                    .map(detalleMapper::toDto)
                    .collect(Collectors.toList());
            arqueoDto.setDetalles(detallesDto);
            return arqueoDto;
        });
        return new PageResponse<>(arqueosDto.getContent(),
                arqueosDto.getTotalPages(),
                arqueosDto.getTotalElements(),
                arqueosDto.getNumber() + 1);
    }

    @Override
    public PageResponse<ArqueoDto> getAllByFecha(LocalDate fecha, int page) {
        PageRequest pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<ArqueoBean> arqueos = arqueoDao.findAllByFechaAndActiveTrue(fecha, pag);
        if (arqueos.isEmpty()){
            throw new NotFoundException("No hay arqueos en la lista");
        }
        Page<ArqueoDto> arqueosDto = arqueos.map(arqueo -> {
            ArqueoDto arqueoDto = mapper.toDto(arqueo);
            List<ArqueoDetalleBean> detalles = arqueoDetalleDao.findByArqueo_IdAndActiveIsTrue(arqueo.getId());
            List<ArqueoDetalleDto> detallesDto = detalles.stream()
                    .map(detalleMapper::toDto)
                    .collect(Collectors.toList());
            arqueoDto.setDetalles(detallesDto);
            return arqueoDto;
        });
        return new PageResponse<>(arqueosDto.getContent(),
                arqueosDto.getTotalPages(),
                arqueosDto.getTotalElements(),
                arqueosDto.getNumber() + 1);
    }



    /*
    Se obtiene la Sesion de Caja
     */
    private SesionCajaBean obtenerSesionCaja(Long sesionCajaId) {
        Optional<SesionCajaBean> sesionCajaOptional = sesionCajaDao.findByIdAndActiveTrue(sesionCajaId);
        if (!sesionCajaOptional.isPresent()) {
            throw new RuntimeException("La sesión de caja con el ID " + sesionCajaId + " no existe.");
        }
        return sesionCajaOptional.get();
    }

/*
Se crea un nuevo Arqueo y se setean los datos
 */
    private ArqueoBean crearArqueo(SesionCajaBean sesionCaja) {
        ArqueoBean arqueo = new ArqueoBean();
        arqueo.setActive(true);
        arqueo.setSesionCaja(sesionCaja);
        arqueo.setFecha(LocalDate.now());
        arqueo.setHora(LocalTime.now());
        arqueo.setMontoTotal(0.0); // Inicializar el monto total
        return arqueo;
    }

    /*
    Se obtienen todos los movimientos de una sesion en especifico
     */
    private List<MovimientoBean> obtenerMovimientos(SesionCajaBean sesionCaja) {
        return movimientoDao.findAllBySesionAndActiveTrue(sesionCaja);
    }

    private void guardarDetalles(List<ArqueoDetalleBean> detalles) {
        detalleService.guardarDetalles(detalles);
    }

    private ArqueoDto crearArqueoDto(ArqueoBean arqueo) {
        ArqueoDto arqueoDto = mapper.toDto(arqueo);
        arqueoDto.setSesionCajaId(arqueo.getSesionCaja().getId());

        // Obtener los detalles del arqueo desde la base de datos
        List<ArqueoDetalleBean> detalles = arqueoDetalleDao.findByArqueo_IdAndActiveIsTrue(arqueo.getId());

        // Mapear los detalles a DTOs
        List<ArqueoDetalleDto> detallesDto = detalles.stream()
                .map(detalleMapper::toDto)
                .collect(Collectors.toList());

        arqueoDto.setDetalles(detallesDto);
        return arqueoDto;
    }

    private double calcularMontoTotal(List<ArqueoDetalleBean> detalles) {
        return detalles.stream()
                .mapToDouble(detalle -> detalle.getMontoEntrada() - detalle.getMontoSalida())
                .sum();
    }


    private void calcularTotales(ArqueoBean arqueo, List<ArqueoDetalleBean> detalles) {
        double totalEntradaTarjeta = calcularTotalEntradaPorTipoPago(detalles, "Tarjeta", true);
        double totalEntradaEfectivo = calcularTotalEntradaPorTipoPago(detalles, "Efectivo", true);
        double totalEntradaTransferencia = calcularTotalEntradaPorTipoPago(detalles, "Transferencia", true);
        double totalSalidaTarjeta = calcularTotalEntradaPorTipoPago(detalles, "Tarjeta", false);
        double totalSalidaEfectivo = calcularTotalEntradaPorTipoPago(detalles, "Efectivo", false);
        double totalSalidaTransferencia = calcularTotalEntradaPorTipoPago(detalles, "Transferencia", false);

        // Establecer los totales calculados en el arqueo
        arqueo.setTotalEntradaTarjeta(totalEntradaTarjeta);
        arqueo.setTotalEntradaEfectivo(totalEntradaEfectivo);
        arqueo.setTotalEntradaTransferencia(totalEntradaTransferencia);
        arqueo.setTotalSalidaTarjeta(totalSalidaTarjeta);
        arqueo.setTotalSalidaEfectivo(totalSalidaEfectivo);
        arqueo.setTotalSalidaTransferencia(totalSalidaTransferencia);
    }

    private double calcularTotalEntradaPorTipoPago(List<ArqueoDetalleBean> detalles, String tipoPago, boolean esEntrada) {
        Set<Long> movimientosProcesados = new HashSet<>();
        return detalles.stream()
                .filter(detalle -> detalle.getMovimiento().isEntrada() == esEntrada)
                .flatMap(detalle -> movimientoDetalleDao.findAllByMovimientoAndActiveTrue(detalle.getMovimiento()).stream())
                .filter(detalleMovimiento -> detalleMovimiento.getTipoDePago().getNombre().equals(tipoPago))
                .filter(detalleMovimiento -> movimientosProcesados.add(detalleMovimiento.getMovimiento().getId())) // Solo procesar el movimiento una vez
                .mapToDouble(MovimientoDetalleBean::getMonto)
                .sum();
    }








    private double calcularMontoPorTipoPago(ArqueoDetalleBean detalle, String tipoPago) {
        return movimientoDetalleDao.findAllByMovimientoAndActiveTrue(detalle.getMovimiento()).stream()
                .filter(detalleMovimiento -> detalleMovimiento.getTipoDePago().getNombre().equals(tipoPago))
                .mapToDouble(MovimientoDetalleBean::getMonto)
                .sum();
    }



}
