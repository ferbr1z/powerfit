package com.devs.powerfit.services.actividades;

import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.daos.actividades.ActividadDao;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.dtos.actividades.ActividadConClientesDto;
import com.devs.powerfit.dtos.actividades.ActividadConEntrenadoresDto;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionConClienteDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.enums.EModalidad;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.services.empleados.EmpleadoService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.actividadMapper.ActividadMapper;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.suscipcioneMapper.SuscripcionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ActividadConEntrenadoresService {
    private ActividadDao actividadDao;
    private SuscripcionDao suscripcionDao;
    private ClienteDao clienteDao;
    private ActividadMapper actividadMapper;
    private ClienteMapper clienteMapper;
    private SuscripcionMapper suscripcionMapper;
    private EmpleadoService empleadoService;
    private ActividadService actividadService;

    public ActividadConEntrenadoresService(ActividadService actividadService, EmpleadoService empleadoService, ActividadDao actividadDao, SuscripcionDao suscripcionDao, ClienteDao clienteDao, ActividadMapper actividadMapper, ClienteMapper clienteMapper, SuscripcionMapper suscripcionMapper) {
        this.actividadDao = actividadDao;
        this.suscripcionDao = suscripcionDao;
        this.clienteDao = clienteDao;
        this.actividadMapper = actividadMapper;
        this.clienteMapper = clienteMapper;
        this.suscripcionMapper = suscripcionMapper;
        this.empleadoService = empleadoService;
        this.actividadService = actividadService;
    }

    public PageResponse<ActividadConEntrenadoresDto> getAllActividadesConEntrenadores(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var actividades = actividadDao.findAllByActiveTrue(pag);
        if (actividades.isEmpty()) {
            throw new NotFoundException("No hay actividades en la lista");
        }

        List<ActividadConEntrenadoresDto> actividadesConEntrenadores = actividades.stream()
                .map(actividad -> {
                    ActividadDto actividadDto = actividadService.getById(actividad.getId());

                    List<EmpleadoDto> entrenadores = actividad.getEntrenadores().stream()
                            .map(entrenador -> empleadoService.getById(entrenador.getId())).toList();

                    return new ActividadConEntrenadoresDto(actividadDto, entrenadores);
                })
                .toList();

        return new PageResponse<>(actividadesConEntrenadores,
                actividades.getTotalPages(),
                actividades.getTotalElements(),
                actividades.getNumber() + 1);
    }

    public ActividadConEntrenadoresDto getByIdWithEntrenadores(Long id) {
        var actividadOptional = actividadDao.findByIdAndActiveTrue(id);
        if (actividadOptional.isPresent()) {
            ActividadBean actividad = actividadOptional.get();

            ActividadConEntrenadoresDto actividadConEntrenadoresDto = new ActividadConEntrenadoresDto();

            ActividadDto actividadDto = actividadService.getById(actividad.getId());

            List<EmpleadoDto> entrenadores = actividad.getEntrenadores().stream()
                    .map(entrenador -> empleadoService.getById(entrenador.getId())).toList();

            actividadConEntrenadoresDto.setActividad(actividadDto);
            actividadConEntrenadoresDto.setEntrenadores(entrenadores);

            return actividadConEntrenadoresDto;
        } else {
            throw new NotFoundException("Actividad no encontrada");
        }
    }

}
