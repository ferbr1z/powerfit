package com.devs.powerfit.services.clientes;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class ClienteService implements IClienteService {

    private ClienteDao clienteDao;
    private ClienteMapper mapper;
    @Autowired
    public ClienteService(ClienteDao clienteDao, ClienteMapper mapper) {
        this.clienteDao = clienteDao;
        this.mapper = mapper;
    }


    @Override
    public ClienteDto create(ClienteDto clienteDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (clienteDto.getNombre() == null || clienteDto.getCedula() == null) {
            throw new BadRequestException("Los campos nombre y cedula son obligatorios para crear un nuevo cliente");
        }

        // Verificar si ya existe un cliente con la misma cédula
        Optional<ClienteBean> existingClientByCedula = clienteDao.findByCedula(clienteDto.getCedula());
        if (existingClientByCedula.isPresent()) {
            ClienteBean clienteByCedula = existingClientByCedula.get();
            if (clienteByCedula.isActive()) {
                throw new BadRequestException("Ya existe un cliente activo con la misma cédula");
            } else {
                // Si el cliente existe pero está inactivo, activarlo
                clienteByCedula.setActive(true);
                clienteDao.save(clienteByCedula);
                return mapper.toDto(clienteByCedula);
            }
        }

        // Verificar si se proporciona un correo electrónico
        if (clienteDto.getEmail() != null) {
            // Verificar si ya existe un cliente con el mismo email
            Optional<ClienteBean> existingClientByEmail = clienteDao.findByEmail(clienteDto.getEmail());
            if (existingClientByEmail.isPresent()) {
                ClienteBean clienteByEmail = existingClientByEmail.get();
                if (clienteByEmail.isActive()) {
                    throw new BadRequestException("Ya existe un cliente activo con el mismo email");
                } else if (!clienteByEmail.getCedula().equals(clienteDto.getCedula())) {
                    // Si el cliente existe pero está inactivo y tiene una cédula diferente, permitir crear un nuevo cliente
                    ClienteBean nuevoCliente = mapper.toBean(clienteDto);
                    nuevoCliente.setActive(true);
                    nuevoCliente.setFechaRegistro(LocalDate.now());
                    clienteDao.save(nuevoCliente);
                    return mapper.toDto(nuevoCliente);
                }
                // Si el cliente existe pero está inactivo y tiene la misma cédula, se comporta como si fuera activo
                clienteByEmail.setActive(true);
                clienteByEmail.setNombre(clienteDto.getNombre()); // Actualizar otros campos si es necesario
                clienteDao.save(clienteByEmail);
                return mapper.toDto(clienteByEmail);
            }
        }

        // Si no existe un cliente activo con la misma cédula o email, crear un nuevo cliente
        ClienteBean nuevoCliente = mapper.toBean(clienteDto);
        nuevoCliente.setActive(true);
        nuevoCliente.setFechaRegistro(LocalDate.now());
        clienteDao.save(nuevoCliente);
        return mapper.toDto(nuevoCliente);
    }



    @Override
    public ClienteDto getById(Long id) {
        var cliente = clienteDao.findByIdAndActiveTrue(id);
        if (cliente.isPresent()) {
            return mapper.toDto(cliente.get());
        }
        throw new NotFoundException("Cliente no encontrado");

    }

    @Override
    public PageResponse<ClienteDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findAllByActiveTrue(pag);

        if (clientes.isEmpty()) {
            throw new NotFoundException("No hay clientes en la lista");
        }

        var clientesDto = clientes.map(cliente -> mapper.toDto(cliente));

        var pageResponse = new PageResponse<ClienteDto>(clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
        return pageResponse;
    }
    @Override
    public ClienteDto update(Long id, ClienteDto clienteDto) {
        var clienteOptional = clienteDao.findByIdAndActiveTrue(id);
        if (clienteOptional.isPresent()) {
            var clienteBean = clienteOptional.get();

            if (clienteDto.getNombre() != null) clienteBean.setNombre(clienteDto.getNombre());
            if (clienteDto.getCedula() != null) clienteBean.setCedula(clienteDto.getCedula());
            if (clienteDto.getRuc() != null) clienteBean.setRuc(clienteDto.getRuc());
            if (clienteDto.getTelefono() != null) clienteBean.setTelefono(clienteDto.getTelefono());
            if (clienteDto.getEmail() != null) clienteBean.setEmail(clienteDto.getEmail());
            if (clienteDto.getDireccion() != null) clienteBean.setDireccion(clienteDto.getDireccion());


            clienteDao.save(clienteBean);

            return mapper.toDto(clienteBean);
        }
        throw new NotFoundException("Cliente no encontrado");
    }


    @Override
    public boolean delete(Long id) {
        var cliente = clienteDao.findByIdAndActiveTrue(id);
        if (cliente.isPresent()) {
            var clienteBean = cliente.get();
            clienteBean.setActive(false);
            clienteDao.save(clienteBean);
            return true;
        }
        throw new NotFoundException("Cliente no encontrado");
    }
    @Override
    public PageResponse<ClienteDto> searchByNombre(String nombre, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findAllByNombreContainingIgnoreCaseAndActiveIsTrue(pag, nombre);

        if (clientes.isEmpty()) {
            throw new NotFoundException("No hay clientes en la lista");
        }

        var clientesDto = clientes.map(cliente -> mapper.toDto(cliente));
        var pageResponse = new PageResponse<ClienteDto>(
                clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
        return pageResponse;
    }


    @Override
    public PageResponse<ClienteDto> searchByCi(String ci, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findByCedulaAndActiveIsTrue(pag, ci);

        if(clientes.isEmpty()){
            throw new NotFoundException("No hay clientes con esa cedula");
        }
        var clientesDto = clientes.map(cliente -> mapper.toDto(cliente));
        var pageResponse = new PageResponse<ClienteDto>(
                clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);

        return pageResponse;
    }

    @Override
    public PageResponse<ClienteDto> searchByRuc(String ruc, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findAllByRucAndActiveTrue(ruc,pag);

        if(clientes.isEmpty()){
            throw new NotFoundException("No hay clientes con ese ruc");
        }
        var clientesDto = clientes.map(cliente -> mapper.toDto(cliente));
        var pageResponse = new PageResponse<ClienteDto>(
                clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);

        return pageResponse;
    }
    public Long countNewClients(LocalDate startOfMonth, LocalDate endOfMonth) {
        if(startOfMonth.isAfter(startOfMonth)){
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        return clienteDao.countClientesByFechaRegistroBetween(startOfMonth, endOfMonth);
    }

}
