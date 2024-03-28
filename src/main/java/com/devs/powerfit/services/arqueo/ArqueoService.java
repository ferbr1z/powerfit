package com.devs.powerfit.services.arqueo;

import com.devs.powerfit.beans.arqueo.ArqueoBean;
import com.devs.powerfit.beans.arqueo.ArqueoDetalleBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.daos.arqueo.ArqueoDao;
import com.devs.powerfit.daos.arqueo.ArqueoDetalleDao;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.daos.movimientos.MovimientoDao;
import com.devs.powerfit.dtos.arqueo.ArqueoDetalleDto;
import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.arqueo.IArqueoService;
import com.devs.powerfit.utils.mappers.arqueoMapper.ArqueoDetalleMapper;
import com.devs.powerfit.utils.mappers.arqueoMapper.ArqueoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArqueoService implements IArqueoService {


    private ArqueoDao arqueoDao;
    private ArqueoDetalleDao arqueoDetalleDao;
    private  MovimientoDao movimientoDao;
    private ArqueoDetalleService arqueoDetalleService;
    private SesionCajaDao sesionCajaDao;
    private ArqueoDetalleMapper arqueoDetalleMapper;
    private ArqueoMapper arqueoMapper;
    @Autowired
    public ArqueoService(ArqueoDao arqueoDao, ArqueoDetalleDao arqueoDetalleDao, MovimientoDao movimientoDao, ArqueoDetalleService arqueoDetalleService, SesionCajaDao sesionCajaDao, ArqueoDetalleMapper arqueoDetalleMapper, ArqueoMapper arqueoMapper) {
        this.arqueoDao = arqueoDao;
        this.arqueoDetalleDao = arqueoDetalleDao;
        this.movimientoDao = movimientoDao;
        this.arqueoDetalleService = arqueoDetalleService;
        this.sesionCajaDao = sesionCajaDao;
        this.arqueoDetalleMapper = arqueoDetalleMapper;
        this.arqueoMapper = arqueoMapper;
    }


    @Override
    public ArqueoDto generarArqueo(ArqueoDto arqueoDto) {
        // Verificar si la sesión de caja existe
        Optional<SesionCajaBean> sesionCajaBean = sesionCajaDao.findByIdAndActiveTrue(arqueoDto.getSesionCajaId());
        if (sesionCajaBean.isEmpty()) {
            throw new NotFoundException("No existe una sesión de caja con ese ID");
        }
        ArqueoBean arqueo = new ArqueoBean();
        Date fechaActual = new Date();
        // Establecer la fecha y la hora en el momento actual
        if (arqueoDto.getFecha() == null){
            arqueo.setFecha(fechaActual);
        }
        if (arqueoDto.getHora() == null){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaActual);
            arqueo.setHora(calendar.getTime());
        }
        arqueo = arqueoMapper.toBean(arqueoDto);

        ArqueoBean arqueoSaved = arqueoDao.save(arqueo);

        return arqueoMapper.toDto(arqueoSaved);
    }
}
