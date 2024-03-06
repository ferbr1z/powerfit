package com.devs.powerfit.services.suscripciones;

import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.dtos.suscripciones.SuscripcionConDetallesDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDetalleDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.interfaces.suscripciones.ISuscripcionConDetalleService;
import com.devs.powerfit.services.clientes.ClienteService;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.suscipciones.SuscripcionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Transactional
@Service
public class SuscripcionConDetalleService implements ISuscripcionConDetalleService {
    private SuscripcionDao suscripcionDao;
    private ClienteService clienteService;
    private SuscripcionMapper mapper;
    private ClienteMapper clienteMapper;
    private SuscripcionDetalleService suscripcionDetalleService;
    private SuscripcionService suscripcionService;
    @Autowired
    public SuscripcionConDetalleService(SuscripcionDao suscripcionDao, ClienteService clienteService, SuscripcionMapper mapper, ClienteMapper clienteMapper, SuscripcionDetalleService suscripcionDetalleService, SuscripcionService suscripcionService) {
        this.suscripcionDao = suscripcionDao;
        this.clienteService = clienteService;
        this.mapper = mapper;
        this.clienteMapper = clienteMapper;
        this.suscripcionDetalleService = suscripcionDetalleService;
        this.suscripcionService = suscripcionService;
    }

    @Override
    public SuscripcionConDetallesDto create(SuscripcionConDetallesDto suscripcionConDetallesDto) {
        SuscripcionDto suscripcionDto = suscripcionConDetallesDto.getSuscripcionDto();
        List<SuscripcionDetalleDto> detallesDtos = suscripcionConDetallesDto.getSuscripcionDetalleDtoList();

        // Verificar si la suscripción principal es nula
        if (suscripcionDto == null) {
            throw new BadRequestException("La suscripción principal es nula" );
        }

        // Verificar si la lista de detalles de suscripción está vacía
        if (detallesDtos == null || detallesDtos.isEmpty()) {
            throw new BadRequestException("La lista de detalles de suscripción está vacía");
        }

        // Crear la suscripción principal
        SuscripcionDto createdSuscripcionDto = suscripcionService.create(suscripcionDto);
        if(createdSuscripcionDto==null){
            throw new BadRequestException("No se pudo crear la cabecera de la suscripcion");
        }
        suscripcionConDetallesDto.setSuscripcionDto(createdSuscripcionDto);

        // Obtener el ID de la suscripción principal creada
        Long suscripcionId = createdSuscripcionDto.getId();
        List<SuscripcionDetalleDto> newList= new ArrayList<>();

        // Iterar sobre cada detalle de suscripción en la lista
        for (SuscripcionDetalleDto detalleDto : detallesDtos) {
            // Verificar si el detalle de suscripción es nulo
            if (detalleDto == null) {
                throw new BadRequestException("Se encontró un detalle de suscripción nulo en la lista");
            }

            // Establecer el ID de la suscripción principal en el detalle de suscripción
            detalleDto.setSubscripcionId(suscripcionId);

            // Crear el detalle de suscripción utilizando el servicio SuscripcionDetalleService
            SuscripcionDetalleDto nuevoItem = suscripcionDetalleService.create(detalleDto);
            newList.add(nuevoItem);
        }
        suscripcionConDetallesDto.setSuscripcionDetalleDtoList(newList);
        // Retornar el objeto SuscripcionConDetallesDto creado
        return suscripcionConDetallesDto;
    }

    @Override
    public SuscripcionConDetallesDto getById(Long id) {
        // Obtener la suscripción principal por su ID
        SuscripcionDto suscripcionDto = suscripcionService.getById(id);

        // Obtener los detalles de suscripción asociados a la suscripción principal
        List<SuscripcionDetalleDto> detallesDtos = suscripcionDetalleService.getAllBySuscripcionId(id);

        // Crear y retornar el objeto SuscripcionConDetallesDto
        return SuscripcionConDetallesDto.builder()
                .suscripcionDto(suscripcionDto)
                .suscripcionDetalleDtoList(detallesDtos)
                .build();
    }

    @Override
    public PageResponse<SuscripcionConDetallesDto> getAll(int page) {
        // Obtener todas las suscripciones principales paginadas
        PageResponse<SuscripcionDto> suscripcionesDtoPage = suscripcionService.getAll(page);

        List<SuscripcionConDetallesDto> suscripcionesConDetallesDtos = new ArrayList<>();

        // Iterar sobre cada suscripción principal y obtener sus detalles de suscripción asociados
        for (SuscripcionDto suscripcionDto : suscripcionesDtoPage.getItems()) {
            List<SuscripcionDetalleDto> detallesDtos = suscripcionDetalleService.getAllBySuscripcionId(suscripcionDto.getId());
            suscripcionesConDetallesDtos.add(SuscripcionConDetallesDto.builder()
                    .suscripcionDto(suscripcionDto)
                    .suscripcionDetalleDtoList(detallesDtos)
                    .build());
        }

        // Crear y retornar la respuesta paginada
        return new PageResponse<>(suscripcionesConDetallesDtos, suscripcionesDtoPage.getTotalPages(), suscripcionesDtoPage.getTotalItems(), suscripcionesDtoPage.getCurrentPage() + 1);
    }

    @Override
    public SuscripcionConDetallesDto update(Long id, SuscripcionConDetallesDto suscripcionConDetallesDto) {
        SuscripcionDto suscripcionDto = suscripcionConDetallesDto.getSuscripcionDto();
        List<SuscripcionDetalleDto> detallesDtos = suscripcionConDetallesDto.getSuscripcionDetalleDtoList();

        // Verificar si la suscripción principal es nula
        if (suscripcionDto == null) {
            throw new BadRequestException("La suscripción principal es nula");
        }

        // Verificar si la lista de detalles de suscripción está vacía
        if (detallesDtos == null || detallesDtos.isEmpty()) {
            throw new BadRequestException("La lista de detalles de suscripción está vacía");
        }

        // Actualizar la suscripción principal
        SuscripcionDto updatedSuscripcionDto = suscripcionService.update(id, suscripcionDto);

        // Obtener el ID de la suscripción principal actualizada
        Long suscripcionId = updatedSuscripcionDto.getId();

        // Iterar sobre cada detalle de suscripción en la lista
        for (SuscripcionDetalleDto detalleDto : detallesDtos) {
            // Verificar si el detalle de suscripción es nulo
            if (detalleDto == null) {
                throw new BadRequestException("Se encontró un detalle de suscripción nulo en la lista");
            }

            // Establecer el ID de la suscripción principal en el detalle de suscripción
            detalleDto.setSubscripcionId(suscripcionId);

            // Actualizar el detalle de suscripción utilizando el servicio SuscripcionDetalleService
            suscripcionDetalleService.update(detalleDto.getId(), detalleDto);
        }

        // Retornar el objeto SuscripcionConDetallesDto actualizado
        return suscripcionConDetallesDto;
    }

    @Override
    public boolean delete(Long id) {
        // Eliminar la suscripción principal
        suscripcionService.delete(id);

        // Eliminar los detalles de suscripción asociados a la suscripción principal
        List<SuscripcionDetalleDto> detallesDtos = suscripcionDetalleService.getAllBySuscripcionId(id);
        for (SuscripcionDetalleDto detalleDto : detallesDtos) {
            suscripcionDetalleService.delete(detalleDto.getId());
        }

        return true;
    }
}
