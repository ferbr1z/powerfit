package com.devs.powerfit.services.suscripciones;

import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.suscripciones.ISuscripcionService;
import com.devs.powerfit.services.clientes.ClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.suscipciones.SuscripcionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SuscripcionService implements ISuscripcionService {
    private SuscripcionDao suscripcionDao;
    private ClienteService clienteService;
    private SuscripcionMapper mapper;
    private ClienteMapper clienteMapper;
    private SuscripcionDetalleService suscripcionDetalleService;
    @Autowired
    public SuscripcionService(SuscripcionDao suscripcionDao, ClienteService clienteService, SuscripcionMapper mapper, ClienteMapper clienteMapper, SuscripcionDetalleService suscripcionDetalleService) {
        this.suscripcionDao = suscripcionDao;
        this.clienteService = clienteService;
        this.mapper = mapper;
        this.clienteMapper = clienteMapper;
        this.suscripcionDetalleService = suscripcionDetalleService;
    }

    @Override
    public SuscripcionDto create(SuscripcionDto suscripcionDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (suscripcionDto.getClienteID() == null) {
            throw new BadRequestException("El campo clienteID es obligatorio para crear una nueva suscripción");
        }

        // Verificar si el cliente existe
        ClienteDto clienteDto = clienteService.getById(suscripcionDto.getClienteID());

        // Crear una instancia de Suscripcion desde SuscripcionDto
        SuscripcionBean suscripcion = new SuscripcionBean();
        suscripcion.setCliente(clienteMapper.toBean(clienteDto));
        suscripcion.setTotal(suscripcionDto.getTotal());
        suscripcion.setActive(true);

        // Guardar la suscripción en la base de datos
        SuscripcionBean savedSuscripcion = suscripcionDao.save(suscripcion);

        // Retornar el SuscripcionDto creado
        return mapper.toDto(savedSuscripcion);
    }

    @Override
    public SuscripcionDto getById(Long id) {
        var suscripcionOptional = suscripcionDao.findByIdAndActiveTrue(id);
        if (suscripcionOptional.isPresent()) {
            var suscripcionBean = suscripcionOptional.get();
            return mapper.toDto(suscripcionBean);
        }
        throw new NotFoundException("Suscripción no encontrada");
    }

    @Override
    public PageResponse<SuscripcionDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var suscripciones = suscripcionDao.findAllByActiveTrue(pag);

        if (suscripciones.isEmpty()) {
            throw new NotFoundException("No hay suscripciones en la lista");
        }

        var suscripcionesDto = suscripciones.map(suscripcion -> mapper.toDto(suscripcion));
        var pageResponse = new PageResponse<SuscripcionDto>(
                suscripcionesDto.getContent(),
                suscripcionesDto.getTotalPages(),
                suscripcionesDto.getTotalElements(),
                suscripcionesDto.getNumber() + 1);

        return pageResponse;
    }
    @Override
    public SuscripcionDto update(Long id, SuscripcionDto suscripcionDto) {
        var suscripcionOptional = suscripcionDao.findByIdAndActiveTrue(id);
        if (suscripcionOptional.isPresent()) {
            var suscripcionBean = suscripcionOptional.get();

            // Actualizar los campos de la suscripción con los valores del DTO
            if (suscripcionDto.getTotal() != null) {
                suscripcionBean.setTotal(suscripcionDto.getTotal());
            }

            // Verificar si se proporciona el ID del cliente para actualizar el cliente asociado
            if (suscripcionDto.getClienteID() != null) {
                // Obtener el cliente asociado a la suscripción
                ClienteDto clienteDto = clienteService.getById(suscripcionDto.getClienteID());

                // Asignar el cliente actualizado a la suscripción
                suscripcionBean.setCliente(clienteMapper.toBean(clienteDto));
            }

            suscripcionDao.save(suscripcionBean);

            return mapper.toDto(suscripcionBean);
        }
        throw new NotFoundException("Suscripción no encontrada");
    }

    @Override
    public boolean delete(Long id) {
        var suscripcionOptional = suscripcionDao.findByIdAndActiveTrue(id);
        if (suscripcionOptional.isPresent()) {
            var suscripcionBean = suscripcionOptional.get();
            // Desactivar la suscripción en lugar de eliminarla físicamente
            suscripcionBean.setActive(false);
            suscripcionDao.save(suscripcionBean);
            return true;
        }
        throw new NotFoundException("Suscripción no encontrada");
    }


    @Override
    public PageResponse<SuscripcionDto> searchByNombreCliente(String nombre, int page) {
        // Buscar clientes por nombre utilizando el servicio de cliente
        PageResponse<ClienteDto> clientesResponse = clienteService.searchByNombre(nombre, page);
        List<ClienteDto> clientes = clientesResponse.getItems();

        if (clientes.isEmpty()) {
            throw new NotFoundException("No se encontraron clientes con ese nombre");
        }

        // Obtener suscripciones para los clientes encontrados
        List<SuscripcionBean> suscripciones = new ArrayList<>();
        clientes.forEach(cliente -> {
            Optional<SuscripcionBean> suscripcion = suscripcionDao.findByClienteIdAndActiveTrue(cliente.getId());
            suscripcion.ifPresent(suscripciones::add);
        });

        if (suscripciones.isEmpty()) {
            throw new NotFoundException("No se encontraron suscripciones para los clientes con ese nombre");
        }

        // Convertir las suscripciones a DTOs
        List<SuscripcionDto> suscripcionesDto = suscripciones.stream()
                .map(suscripcion -> mapper.toDto(suscripcion))
                .collect(Collectors.toList());
        // Crear y retornar la respuesta de la página
        return new PageResponse<>(suscripcionesDto, clientesResponse.getTotalPages(), clientesResponse.getTotalItems(), page);
    }



}