package com.devs.powerfit.services.arqueo;

import com.devs.powerfit.beans.arqueo.ArqueoBean;
import com.devs.powerfit.beans.arqueo.ArqueoDetalleBean;
import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.beans.movimientos.MovimientoDetalleBean;
import com.devs.powerfit.daos.arqueo.ArqueoDao;
import com.devs.powerfit.daos.arqueo.ArqueoDetalleDao;
import com.devs.powerfit.daos.movimientos.MovimientoDetalleDao;
import com.devs.powerfit.dtos.arqueo.ArqueoDetalleDto;
import com.devs.powerfit.dtos.movimientos.MovimientoDetalleDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.arqueo.IArqueoDetallesService;
import com.devs.powerfit.utils.mappers.arqueoMapper.ArqueoDetalleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArqueoDetalleService implements IArqueoDetallesService {
    private MovimientoDetalleDao movimientoDetalle;
    private ArqueoDetalleMapper arqueoDetalleMapper;
    private ArqueoDetalleDao arqueoDetalleDao;

    private ArqueoDao arqueoDao;

    @Autowired
    public ArqueoDetalleService(MovimientoDetalleDao movimientoDetalle, ArqueoDetalleMapper arqueoDetalleMapper, ArqueoDetalleDao arqueoDetalleDao, ArqueoDao arqueoDao) {
        this.movimientoDetalle = movimientoDetalle;
        this.arqueoDetalleMapper = arqueoDetalleMapper;
        this.arqueoDetalleDao = arqueoDetalleDao;
        this.arqueoDao = arqueoDao;
    }

    @Override
    public ArqueoDetalleDto crearArqueoDetalle(ArqueoDetalleDto arqueoDetalleDto) {
        Optional<MovimientoDetalleBean> movimientoDetalleBean = movimientoDetalle.findByIdAndActiveTrue(arqueoDetalleDto.getMovimientoId());
        if (!movimientoDetalleBean.isPresent()) {
            throw new NotFoundException("No se encontró detalle de movimiento para el movimiento con ID");
        }
//        // Asignar los valores de monto de entrada y salida según el parámetro de entrada
//        arqueoDetalleDto.setMontoEntrada(entrada ? movimientoDetalleBean.get().getMonto() : null);
//        arqueoDetalleDto.setMontoSalida(entrada ? null : movimientoDetalleBean.get().getMonto());

        // Mapear el DTO de detalle de arqueo a un objeto de entidad ArqueoDetalleBean
        ArqueoDetalleBean arqueoDetalleBean = arqueoDetalleMapper.toBean(arqueoDetalleDto);
        arqueoDetalleBean.setActive(true);
        Optional<ArqueoBean> arqueoBean = arqueoDao.findByIdAndActiveIsTrue(arqueoDetalleDto.getArqueoId());
        arqueoDetalleBean.setArqueo(arqueoBean.get());

        arqueoDetalleDao.save(arqueoDetalleBean);

        return arqueoDetalleMapper.toDto(arqueoDetalleBean);


    }

    @Override
    public List<ArqueoDetalleDto> getDetalles(Long id) {
        List<ArqueoDetalleBean> detalles = arqueoDetalleDao.findByArqueo_IdAndActiveIsTrue(id);
        return toDtoList(detalles);
    }

    private List<ArqueoDetalleDto> toDtoList(List<ArqueoDetalleBean> detalles){
        return detalles.stream().map(arqueoDetalleMapper::toDto).collect(Collectors.toList());
    }


}
