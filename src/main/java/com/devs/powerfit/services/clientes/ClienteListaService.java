package com.devs.powerfit.services.clientes;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.daos.suscripciones.SuscripcionDao;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.clientes.ClienteListaDto;
import com.devs.powerfit.dtos.clientes.NuevosClientesDto;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteListaService {
    private ClienteDao clienteDao;
    private SuscripcionDao suscripcionDao;
    private IClienteService clienteService;
    private ClienteMapper clienteMapper;
    @Autowired
    public ClienteListaService(ClienteDao clienteDao, SuscripcionDao suscripcionDao, IClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteDao = clienteDao;
        this.suscripcionDao = suscripcionDao;
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }
    public PageResponse<ClienteListaDto> obtenerTodosClientesConEstadoGeneral(int pagina) {
        var pag = PageRequest.of(pagina - 1, Setting.PAGE_SIZE);
        Page<ClienteBean> clientes = clienteDao.findAllByActiveTrue(pag);

        if (clientes.isEmpty()) {
            throw new NotFoundException("No hay clientes en la lista");
        }

        var clienteListaDtos = clientes.map(this::construirClienteListaDto);

        return new PageResponse<>(clienteListaDtos.getContent(),
                clienteListaDtos.getTotalPages(),
                clienteListaDtos.getTotalElements(),
                clienteListaDtos.getNumber()+1);
    }

    private ClienteListaDto construirClienteListaDto(ClienteBean cliente) {
        ClienteListaDto clienteListaDto = new ClienteListaDto();
        clienteListaDto.setNombre(cliente.getNombre());
        clienteListaDto.setId(cliente.getId());
        clienteListaDto.setActive(cliente.isActive());
        clienteListaDto.setRuc(cliente.getRuc());
        clienteListaDto.setCedula(cliente.getCedula());
        clienteListaDto.setTelefono(cliente.getTelefono());
        clienteListaDto.setEmail(cliente.getEmail());
        clienteListaDto.setDireccion(cliente.getDireccion());
        clienteListaDto.setFechaRegistro(cliente.getFechaRegistro());

        // Comprobar si el cliente tiene suscripción pendiente
        boolean tieneSuscripcionPendiente = suscripcionDao.existsByClienteAndEstadoAndActiveTrue(cliente, EEstado.PENDIENTE);

        // Comprobar si el cliente tiene suscripción pagada
        boolean tieneSuscripcionPagada = suscripcionDao.existsByClienteAndEstadoAndActiveTrue(cliente, EEstado.PAGADO);

        // Establecer el estado
        if (tieneSuscripcionPendiente) {
            clienteListaDto.setEstado(EEstado.PENDIENTE);
        } else if (tieneSuscripcionPagada) {
            clienteListaDto.setEstado(EEstado.PAGADO);
        } else {
            clienteListaDto.setEstado(null); // Cliente sin suscripción
        }

        return clienteListaDto;
    }
    public NuevosClientesDto obtenerClientesNuevos(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new BadRequestException("Las fechas de inicio y fin deben ser especificadas.");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new BadRequestException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        NuevosClientesDto nuevos = new NuevosClientesDto();
        nuevos.setCantidadNuevosClientes(clienteDao.countByFechaRegistroBetweenAndActiveTrue(fechaInicio, fechaFin));

        return nuevos;
    }

    public PageResponse<ClienteDto> obtenerClientesNuevosConDetalles(int page , LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new BadRequestException("Las fechas de inicio y fin deben ser especificadas.");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new BadRequestException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findAllByFechaRegistroBetweenAndActiveTrue(pag, fechaInicio, fechaFin);

        var clientesDto = clientes.map(cliente -> clienteMapper.toDto(cliente));

        var pageResponse = new PageResponse<ClienteDto>(clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
        return pageResponse;
    }
}
