package com.devs.powerfit.services.suscripciones;

import com.devs.powerfit.beans.suscripciones.SuscripcionDetalleBean;
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

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public class SuscripcionDetalleService implements ISuscripcionDetalleService {
    private SuscripcionDetalleDao suscripcionDetalleDao;
    private IActividadService actividadService;
    private ActividadMapper actividadMapper;
    private SuscripcionDetalleMapper mapper;
    private SuscripcionMapper suscripcionMapper;
    private SuscripcionService suscripcionService;
    private CacheManager cacheManager;
    @Autowired
    public SuscripcionDetalleService(SuscripcionDetalleDao suscripcionDetalleDao, IActividadService actividadService, ActividadMapper actividadMapper, SuscripcionDetalleMapper mapper, SuscripcionMapper suscripcionMapper, SuscripcionService suscripcionService, CacheManager cacheManager) {
        this.suscripcionDetalleDao = suscripcionDetalleDao;
        this.actividadService = actividadService;
        this.actividadMapper = actividadMapper;
        this.mapper = mapper;
        this.suscripcionMapper = suscripcionMapper;
        this.suscripcionService = suscripcionService;
        this.cacheManager = cacheManager;
    }

    @Override
    public SuscripcionDetalleDto create(SuscripcionDetalleDto suscripcionDetalleDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (suscripcionDetalleDto.getActividadId() == null || suscripcionDetalleDto.getSubscripcionId()==null) {
            throw new BadRequestException("El campo actividadID y suscripcionId son obligatorios para crear una nueva suscripciónDetalle");
        }
        //Verificar si la suscripcion existe
        SuscripcionDto suscripcionDto=suscripcionService.getById(suscripcionDetalleDto.getSubscripcionId());
        // Verificar si el actividad existe
        ActividadDto actividadDto = actividadService.getById(suscripcionDetalleDto.getActividadId());
        SuscripcionDetalleBean suscripcion = new SuscripcionDetalleBean();
        // Convertir el valor del campo modalidad del DTO a un objeto EModalidad
        EModalidad modalidad = EModalidad.valueOf(suscripcionDetalleDto.getModalidad());
        suscripcion.setActividad(actividadMapper.toBean(actividadDto));
        suscripcion.setSuscripcion(suscripcionMapper.toBean(suscripcionDto));
        suscripcion.setEstado(EEstado.valueOf(suscripcionDetalleDto.getEstado()));
        suscripcion.setModalidad(modalidad);
        suscripcion.setFechaInicio(new Date()); // Utilizamos el constructor sin argumentos para obtener la fecha actual

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(suscripcion.getFechaInicio());

// Verificar si la modalidad es MENSUAL
        if (modalidad == EModalidad.MENSUAL) {
            calendar.add(Calendar.MONTH, 1);
        } else {
            // Si no es MENSUAL, entonces es SEMANAL
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        Date fechaFin = calendar.getTime();

        suscripcion.setFechaFin(fechaFin);
        suscripcion.setActive(true);

        // Guardar la suscripción en la base de datos
        SuscripcionDetalleBean savedSuscripcion = suscripcionDetalleDao.save(suscripcion);

        // Retornar el suscripcionDetalleDto creado
        return mapper.toDto(savedSuscripcion);
    }
    @Cacheable(cacheNames = "IS::api_suscripcion_detalles", key = "'suscripcion_detalle_'+#id")
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
        // Cachear manualmente cada suscripcion en Redis
        for (SuscripcionDetalleDto suscripcionDto : suscripcionesDto) {
            String cacheName = "sd::api_suscripcion_detalles";
            String key = "suscripcion_detalle_" + suscripcionDto.getId();
            Cache cache = cacheManager.getCache(cacheName);

            // Verificar si la actividad ya está en la caché
            Cache.ValueWrapper valueWrapper = cache.get(key);

            if (valueWrapper == null) {
                // Si no está en la caché, cachearla
                cache.put(key, suscripcionDto);
            }
        }
        var pageResponse = new PageResponse<SuscripcionDetalleDto>(
                suscripcionesDto.getContent(),
                suscripcionesDto.getTotalPages(),
                suscripcionesDto.getTotalElements(),
                suscripcionesDto.getNumber() + 1);

        return pageResponse;
    }
    @CachePut(cacheNames = "IS::api_suscripcion_detalles", key = "'suscripcion_detalle_'+#id")
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
                SuscripcionDto suscripcionDto = suscripcionService.getById(suscripcionDetalleDto.getSubscripcionId());

                // Asignar la suscripción actualizada al detalle de suscripción
                suscripcionDetalleBean.setSuscripcion(suscripcionMapper.toBean(suscripcionDto));
            }

            suscripcionDetalleDao.save(suscripcionDetalleBean);

            return mapper.toDto(suscripcionDetalleBean);
        }
        throw new NotFoundException("Detalle de suscripción no encontrado");
    }


    @CacheEvict(cacheNames = "IS::api_suscripcion_detalles", key = "'suscripcion_detalle_'+#id")
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
        throw new NotFoundException("Detalle de suscripción no encontrado");
    }
}
