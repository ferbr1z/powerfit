package com.devs.powerfit.services.suscripciones;

import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.enums.EModalidad;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.actividades.IActividadService;
import com.devs.powerfit.interfaces.suscripciones.ISuscripcionDetalleService;
import com.devs.powerfit.services.clientes.ClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.actividadMapper.ActividadMapper;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.suscipcioneMapper.SuscripcionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class SuscripcionService implements ISuscripcionDetalleService {
    private SuscripcionDao suscripcionDetalleDao;
    private IActividadService actividadService;
    private ActividadMapper actividadMapper;
    private SuscripcionMapper mapper;
    private ClienteService clienteService;
    private ClienteMapper clienteMapper;

    @Autowired
    public SuscripcionService(SuscripcionDao suscripcionDetalleDao, IActividadService actividadService, ActividadMapper actividadMapper, SuscripcionMapper mapper, SuscripcionMapper suscripcionMapper, SuscripcionDao suscripcionDao, ClienteService clienteService, ClienteMapper clienteMapper) {
        this.suscripcionDetalleDao = suscripcionDetalleDao;
        this.actividadService = actividadService;
        this.actividadMapper = actividadMapper;
        this.mapper = mapper;
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }

    @Override
    public SuscripcionDto create(SuscripcionDto suscripcionDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (suscripcionDto.getActividadId() == null || suscripcionDto.getClienteId() == null) {
            throw new BadRequestException("El campo actividadID y clienteId son obligatorios para crear una nueva suscripción");
        }

        // Verificar si el cliente existe
        ClienteDto clienteDto = clienteService.getById(suscripcionDto.getClienteId());
        if (clienteDto == null) {
            throw new NotFoundException("Cliente no encontrado con el ID proporcionado: " + suscripcionDto.getClienteId());
        }

        // Verificar si la actividad existe
        ActividadDto actividadDto = actividadService.getById(suscripcionDto.getActividadId());
        if (actividadDto == null) {
            throw new NotFoundException("Actividad no encontrada con el ID proporcionado: " + suscripcionDto.getActividadId());
        }
        // Verificar si ya existe una suscripción activa para el mismo cliente y la misma actividad que no esté pagada
        if (suscripcionDetalleDao.existsByClienteAndActividadAndEstado(clienteMapper.toBean(clienteDto), actividadMapper.toBean(actividadDto), EEstado.PENDIENTE)){
            throw new BadRequestException("No se puede crear una nueva suscripción porque ya existe una suscripción pendiente de pago para el mismo cliente y la misma actividad.");
        }

        // Convertir el valor del campo modalidad del DTO a un objeto EModalidad
        EModalidad modalidad = EModalidad.valueOf(suscripcionDto.getModalidad());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        // Obtener la fecha de inicio
        Date fechaInicio;
        if (suscripcionDto.getFechaInicio() != null) {
            try {
                fechaInicio = sdf.parse(sdf.format(suscripcionDto.getFechaInicio()));
            } catch (ParseException e) {
                throw new BadRequestException("Formato de fecha de inicio inválido: " + suscripcionDto.getFechaInicio());
            }
        } else {
            fechaInicio = new Date();
        }

        // Crear el detalle de suscripcion
        SuscripcionBean suscripcionDetalle = new SuscripcionBean();
        suscripcionDetalle.setCliente(clienteMapper.toBean(clienteDto));
        suscripcionDetalle.setActividad(actividadMapper.toBean(actividadDto));
        suscripcionDetalle.setEstado(EEstado.valueOf(suscripcionDto.getEstado()));
        suscripcionDetalle.setModalidad(modalidad);
        suscripcionDetalle.setFechaInicio(fechaInicio);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fechaInicio);

        // Verificar si la modalidad es MENSUAL
        if (modalidad == EModalidad.MENSUAL) {
            calendar.add(Calendar.MONTH, 1);
        } else {
            // Si no es MENSUAL, entonces es SEMANAL
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // Calcular la fecha de fin
        Date fechaFin = calendar.getTime();
        try {
            suscripcionDetalle.setFechaFin(sdf.parse(sdf.format(fechaFin)));
        } catch (ParseException e) {
            throw new BadRequestException("Formato de fecha de inicio inválido: " + suscripcionDto.getFechaInicio());
        }
        suscripcionDetalle.setActive(true); // Establecer como activa

        // Guardar el detalle de suscripcion en la base de datos
        SuscripcionBean savedSuscripcion = suscripcionDetalleDao.save(suscripcionDetalle);
        SuscripcionDto detalleCreado = mapper.toDto(savedSuscripcion);
        // Retornar el detalle de suscripcion creado
        return detalleCreado;
    }



    @Override
    public SuscripcionDto getById(Long id) {
        var suscripcionDetalleOptional = suscripcionDetalleDao.findByIdAndActiveTrue(id);
        if (suscripcionDetalleOptional.isPresent()) {
            var suscripcionDetalleBean = suscripcionDetalleOptional.get();
            return mapper.toDto(suscripcionDetalleBean);
        }
        throw new NotFoundException("Detalle de suscripción no encontrado");
    }

    @Override
    public PageResponse<SuscripcionDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var suscripcionDetalles = suscripcionDetalleDao.findAllByActiveTrue(pag);

        if (suscripcionDetalles.isEmpty()) {
            throw new NotFoundException("No hay detalles de suscripciones en la lista");
        }

        var suscripcionesDto = suscripcionDetalles.map(suscripcionDetalle -> mapper.toDto(suscripcionDetalle));
        var pageResponse = new PageResponse<SuscripcionDto>(
                suscripcionesDto.getContent(),
                suscripcionesDto.getTotalPages(),
                suscripcionesDto.getTotalElements(),
                suscripcionesDto.getNumber() + 1);

        return pageResponse;
    }
    @Override
    public SuscripcionDto update(Long id, SuscripcionDto suscripcionDto) {
        var suscripcionDetalleOptional = suscripcionDetalleDao.findByIdAndActiveTrue(id);
        if (suscripcionDetalleOptional.isPresent()) {
            var suscripcionDetalleBean = suscripcionDetalleOptional.get();

            // Actualizar los campos del detalle de suscripción con los valores del DTO
            if (suscripcionDto.getModalidad() != null) {
                EModalidad modalidad = EModalidad.valueOf(suscripcionDto.getModalidad());
                suscripcionDetalleBean.setModalidad(modalidad);

                // Actualizar la fecha de inicio si se proporciona en el DTO
                if (suscripcionDto.getFechaInicio() != null) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date fechaInicio = sdf.parse(sdf.format(suscripcionDto.getFechaInicio()));
                        suscripcionDetalleBean.setFechaInicio(fechaInicio);

                        // Recalcular la fecha de fin basada en la nueva fecha de inicio y la modalidad
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(fechaInicio);
                        int field = (modalidad == EModalidad.MENSUAL) ? Calendar.MONTH : Calendar.WEEK_OF_YEAR;
                        calendar.add(field, 1);
                        Date fechaFin = calendar.getTime();
                        suscripcionDetalleBean.setFechaFin(sdf.parse(sdf.format(fechaFin)));
                    } catch (ParseException e) {
                        throw new BadRequestException("Formato de fecha de inicio inválido: " + suscripcionDto.getFechaInicio());
                    }
                }
            }

            if (suscripcionDto.getEstado() != null) {
                suscripcionDetalleBean.setEstado(EEstado.valueOf(suscripcionDto.getEstado()));
            }

            // Verificar si se proporciona el ID de la actividad para actualizar la actividad asociada
            if (suscripcionDto.getActividadId() != null) {
                // Obtener la actividad asociada al detalle de suscripción
                ActividadDto actividadDto = actividadService.getById(suscripcionDto.getActividadId());

                // Asignar la actividad actualizada al detalle de suscripción
                suscripcionDetalleBean.setActividad(actividadMapper.toBean(actividadDto));
            }

            suscripcionDetalleDao.save(suscripcionDetalleBean);

            return mapper.toDto(suscripcionDetalleBean);
        }
        throw new NotFoundException("Detalle de suscripción no encontrado");
    }

    @Override
    public boolean delete(Long id) {

        var suscripcionOptional = suscripcionDetalleDao.findByIdAndActiveTrue(id);
        if (suscripcionOptional.isPresent()) {
            var suscripcionBean = suscripcionOptional.get();
            // Desactivar la suscripción en lugar de eliminarla físicamente
            suscripcionBean.setActive(false);
            suscripcionDetalleDao.save(suscripcionBean);
            return true;
        }
        throw new NotFoundException("Suscripción no encontrada" );
    }

    public PageResponse<SuscripcionDto> getAllByClientId(Long id,int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var suscripcionDetalles = suscripcionDetalleDao.findAllByClienteIdAndActiveTrue(pag,id);

        if (suscripcionDetalles.isEmpty()) {
            throw new NotFoundException("No hay  suscripciones de ese clienteId en la lista");
        }

        var suscripcionesDto = suscripcionDetalles.map(suscripcionDetalle -> mapper.toDto(suscripcionDetalle));
        var pageResponse = new PageResponse<SuscripcionDto>(
                suscripcionesDto.getContent(),
                suscripcionesDto.getTotalPages(),
                suscripcionesDto.getTotalElements(),
                suscripcionesDto.getNumber() + 1);

        return pageResponse;
    }
    public List<SuscripcionDto> createList(List<SuscripcionDto> suscripcionDtoList) {
        List<SuscripcionDto> suscripcionesCreadas = new ArrayList<>();

        for (SuscripcionDto suscripcionDto : suscripcionDtoList) {
            SuscripcionDto suscripcionCreada = create(suscripcionDto);
            suscripcionesCreadas.add(suscripcionCreada);
        }

        return suscripcionesCreadas;
    }
    public PageResponse<SuscripcionDto> getAllPendientesByClientId(Long id, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var suscripcionDetalles = suscripcionDetalleDao.findAllByClienteIdAndEstadoAndActiveTrue(pag, id, EEstado.PENDIENTE);

        if (suscripcionDetalles.isEmpty()) {
            throw new NotFoundException("No hay suscripciones pendientes para este cliente");
        }

        var suscripcionesDto = suscripcionDetalles.map(suscripcionDetalle -> mapper.toDto(suscripcionDetalle));
        var pageResponse = new PageResponse<SuscripcionDto>(
                suscripcionesDto.getContent(),
                suscripcionesDto.getTotalPages(),
                suscripcionesDto.getTotalElements(),
                suscripcionesDto.getNumber() + 1);

        return pageResponse;
    }
    public PageResponse<SuscripcionDto> getAllPagadosByClientId(Long id, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var suscripcionDetalles = suscripcionDetalleDao.findAllByClienteIdAndEstadoAndActiveTrue(pag, id, EEstado.PAGADO);

        if (suscripcionDetalles.isEmpty()) {
            throw new NotFoundException("No hay suscripciones pagadas para este cliente");
        }

        var suscripcionesDto = suscripcionDetalles.map(suscripcionDetalle -> mapper.toDto(suscripcionDetalle));
        var pageResponse = new PageResponse<SuscripcionDto>(
                suscripcionesDto.getContent(),
                suscripcionesDto.getTotalPages(),
                suscripcionesDto.getTotalElements(),
                suscripcionesDto.getNumber() + 1);

        return pageResponse;
    }
    public PageResponse<SuscripcionDto> getAllPagados( int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var suscripcionDetalles = suscripcionDetalleDao.findAllByEstadoAndActiveTrue(pag,  EEstado.PAGADO);

        if (suscripcionDetalles.isEmpty()) {
            throw new NotFoundException("No hay suscripciones pagadas");
        }

        var suscripcionesDto = suscripcionDetalles.map(suscripcionDetalle -> mapper.toDto(suscripcionDetalle));
        var pageResponse = new PageResponse<SuscripcionDto>(
                suscripcionesDto.getContent(),
                suscripcionesDto.getTotalPages(),
                suscripcionesDto.getTotalElements(),
                suscripcionesDto.getNumber() + 1);

        return pageResponse;
    }
    public PageResponse<SuscripcionDto> getAllPendientes( int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var suscripcionDetalles = suscripcionDetalleDao.findAllByEstadoAndActiveTrue(pag,  EEstado.PENDIENTE);

        if (suscripcionDetalles.isEmpty()) {
            throw new NotFoundException("No hay suscripciones pendientes");
        }

        var suscripcionesDto = suscripcionDetalles.map(suscripcionDetalle -> mapper.toDto(suscripcionDetalle));
        var pageResponse = new PageResponse<SuscripcionDto>(
                suscripcionesDto.getContent(),
                suscripcionesDto.getTotalPages(),
                suscripcionesDto.getTotalElements(),
                suscripcionesDto.getNumber() + 1);

        return pageResponse;
    }
}
