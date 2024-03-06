package com.devs.powerfit.services.suscripciones;

import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionDetalleBean;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.daos.suscripciones.SuscripcionDetalleDao;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDetalleDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.enums.EModalidad;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.actividades.IActividadService;
import com.devs.powerfit.interfaces.suscripciones.ISuscripcionDetalleService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.actividadMapper.ActividadMapper;
import com.devs.powerfit.utils.mappers.suscipciones.SuscripcionDetalleMapper;
import com.devs.powerfit.utils.mappers.suscipciones.SuscripcionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SuscripcionDetalleService implements ISuscripcionDetalleService {
    private SuscripcionDetalleDao suscripcionDetalleDao;
    private IActividadService actividadService;
    private ActividadMapper actividadMapper;
    private SuscripcionDetalleMapper mapper;
    private SuscripcionMapper suscripcionMapper;

    private SuscripcionDao suscripcionDao;
    @Autowired
    public SuscripcionDetalleService(SuscripcionDetalleDao suscripcionDetalleDao, IActividadService actividadService, ActividadMapper actividadMapper, SuscripcionDetalleMapper mapper, SuscripcionMapper suscripcionMapper,  SuscripcionDao suscripcionDao) {
        this.suscripcionDetalleDao = suscripcionDetalleDao;
        this.actividadService = actividadService;
        this.actividadMapper = actividadMapper;
        this.mapper = mapper;
        this.suscripcionMapper = suscripcionMapper;
        this.suscripcionDao = suscripcionDao;
    }

    @Override
    public SuscripcionDetalleDto create(SuscripcionDetalleDto suscripcionDetalleDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (suscripcionDetalleDto.getActividadId() == null || suscripcionDetalleDto.getSubscripcionId() == null) {
            throw new BadRequestException("El campo actividadID y suscripcionId son obligatorios para crear una nueva suscripciónDetalle");
        }

        // Verificar si la suscripcion existe
        Optional<SuscripcionBean> suscripcionOpcional = suscripcionDao.findByIdAndActiveTrue(suscripcionDetalleDto.getSubscripcionId());
        if (suscripcionOpcional.isEmpty()) {
            throw new BadRequestException("No existe suscripcion con ese ID");
        }
        System.out.println(suscripcionOpcional.get());
        // Obtener la suscripcion completa
        SuscripcionBean suscripcion = suscripcionOpcional.get();
        System.out.println(suscripcion);
        // Verificar si el actividad existe
        ActividadDto actividadDto = actividadService.getById(suscripcionDetalleDto.getActividadId());

        // Convertir el valor del campo modalidad del DTO a un objeto EModalidad
        EModalidad modalidad = EModalidad.valueOf(suscripcionDetalleDto.getModalidad());

        // Crear el detalle de suscripcion
        SuscripcionDetalleBean suscripcionDetalle = new SuscripcionDetalleBean();
        suscripcionDetalle.setSuscripcion(suscripcion); // Establecer la suscripcion en el detalle de suscripcion
        suscripcionDetalle.setActividad(actividadMapper.toBean(actividadDto));
        suscripcionDetalle.setEstado(EEstado.valueOf(suscripcionDetalleDto.getEstado()));
        suscripcionDetalle.setModalidad(modalidad);
        suscripcionDetalle.setFechaInicio(new Date()); // Utilizamos el constructor sin argumentos para obtener la fecha actual

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(suscripcionDetalle.getFechaInicio());

        // Verificar si la modalidad es MENSUAL
        if (modalidad == EModalidad.MENSUAL) {
            calendar.add(Calendar.MONTH, 1);
        } else {
            // Si no es MENSUAL, entonces es SEMANAL
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // Calcular la fecha de fin
        Date fechaFin = calendar.getTime();
        suscripcionDetalle.setFechaFin(fechaFin);
        suscripcionDetalle.setActive(true);
        System.out.println(suscripcionDetalle);
        // Guardar el detalle de suscripcion en la base de datos
        SuscripcionDetalleBean savedSuscripcion = suscripcionDetalleDao.save(suscripcionDetalle);
        SuscripcionDetalleDto detalleCreado = mapper.toDto(savedSuscripcion);
        detalleCreado.setSubscripcionId(suscripcionDetalleDto.getSubscripcionId());
        System.out.println(detalleCreado);

        // Retornar el detalle de suscripcion creado
        return detalleCreado;
    }

    @Override
    public SuscripcionDetalleDto getById(Long id) {
        var suscripcionDetalleOptional = suscripcionDetalleDao.findByIdAndActiveTrue(id);
        if (suscripcionDetalleOptional.isPresent()) {
            var suscripcionDetalleBean = suscripcionDetalleOptional.get();
            return mapper.toDto(suscripcionDetalleBean);
        }
        throw new NotFoundException("Detalle de suscripción no encontrado");
    }

    @Override
    public PageResponse<SuscripcionDetalleDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var suscripcionDetalles = suscripcionDetalleDao.findAllByActiveTrue(pag);

        if (suscripcionDetalles.isEmpty()) {
            throw new NotFoundException("No hay detalles de suscripciones en la lista");
        }

        var suscripcionesDto = suscripcionDetalles.map(suscripcionDetalle -> mapper.toDto(suscripcionDetalle));
        var pageResponse = new PageResponse<SuscripcionDetalleDto>(
                suscripcionesDto.getContent(),
                suscripcionesDto.getTotalPages(),
                suscripcionesDto.getTotalElements(),
                suscripcionesDto.getNumber() + 1);

        return pageResponse;
    }
    @Override
    public SuscripcionDetalleDto update(Long id, SuscripcionDetalleDto suscripcionDetalleDto) {
        var suscripcionDetalleOptional = suscripcionDetalleDao.findByIdAndActiveTrue(id);
        if (suscripcionDetalleOptional.isPresent()) {
            var suscripcionDetalleBean = suscripcionDetalleOptional.get();

            // Actualizar los campos del detalle de suscripción con los valores del DTO
            if (suscripcionDetalleDto.getModalidad() != null) {
                EModalidad modalidad = EModalidad.valueOf(suscripcionDetalleDto.getModalidad());
                suscripcionDetalleBean.setModalidad(modalidad);

                // Actualizar la fecha de inicio si se proporciona en el DTO
                if (suscripcionDetalleDto.getFechaInicio() != null) {
                    suscripcionDetalleBean.setFechaInicio(suscripcionDetalleDto.getFechaInicio());

                    // Recalcular la fecha de fin basada en la nueva fecha de inicio y la modalidad
                    Date fechaInicio = suscripcionDetalleDto.getFechaInicio();
                    if (modalidad == EModalidad.MENSUAL) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fechaInicio);
                        calendar.add(Calendar.MONTH, 1);
                        suscripcionDetalleBean.setFechaFin(calendar.getTime());
                    } else {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fechaInicio);
                        calendar.add(Calendar.WEEK_OF_YEAR, 1);
                        suscripcionDetalleBean.setFechaFin(calendar.getTime());
                    }
                }
            }
            if (suscripcionDetalleDto.getEstado() != null) {
                suscripcionDetalleBean.setEstado(EEstado.valueOf(suscripcionDetalleDto.getEstado()));
            }

            // Verificar si se proporciona el ID de la actividad para actualizar la actividad asociada
            if (suscripcionDetalleDto.getActividadId() != null) {
                // Obtener la actividad asociada al detalle de suscripción
                ActividadDto actividadDto = actividadService.getById(suscripcionDetalleDto.getActividadId());

                // Asignar la actividad actualizada al detalle de suscripción
                suscripcionDetalleBean.setActividad(actividadMapper.toBean(actividadDto));
            }

            // Verificar si se proporciona el ID de la suscripción para actualizar la suscripción asociada
            if (suscripcionDetalleDto.getSubscripcionId() != null) {
                // Obtener la suscripción asociada al detalle de suscripción
                SuscripcionDto suscripcionDto = suscripcionMapper.toDto(suscripcionDao.getById(suscripcionDetalleDto.getSubscripcionId()));
                if(suscripcionDto==null){
                    throw new BadRequestException("No existe suscripcion con ese ID");
                }
                // Asignar la suscripción actualizada al detalle de suscripción
                suscripcionDetalleBean.setSuscripcion(suscripcionMapper.toBean(suscripcionDto));
            }

            suscripcionDetalleDao.save(suscripcionDetalleBean);

            return mapper.toDto(suscripcionDetalleBean);
        }
        throw new NotFoundException("Detalle de suscripción no encontrado");
    }


    @Override
    public boolean delete(Long id) {
        var suscripcionDetalleOptional = suscripcionDetalleDao.findByIdAndActiveTrue(id);
        if (suscripcionDetalleOptional.isPresent()) {
            var suscripcionDetalleBean = suscripcionDetalleOptional.get();
            // Desactivar el detalle de suscripción en lugar de eliminarlo físicamente
            suscripcionDetalleBean.setActive(false);
            suscripcionDetalleDao.save(suscripcionDetalleBean);
            return true;
        }
        throw new NotFoundException("Detalle de suscripción no encontrado" );
    }
    public List<SuscripcionDetalleDto> getAllBySuscripcionId( Long id) {
        var suscripcionDetalles = suscripcionDetalleDao.findAllBySuscripcionIdAndActiveTrue(id);

        if (suscripcionDetalles.isEmpty()) {
            throw new NotFoundException("No hay detalles de suscripciones de ese id en la lista");
        }

        List<SuscripcionDetalleDto> suscripcionesDto = suscripcionDetalles.stream()
                .map(suscripcionDetalle -> mapper.toDto(suscripcionDetalle))
                .collect(Collectors.toList());

        return suscripcionesDto;
    }
}
