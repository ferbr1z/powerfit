package com.devs.powerfit.services.clientes;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.daos.clientes.ClienteDao;
import com.devs.powerfit.dtos.auth.UsuarioDto;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import com.devs.powerfit.services.auth.AuthService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class ClienteService implements IClienteService {

    private final ClienteDao clienteDao;
    private final ClienteMapper mapper;
    private final AuthService authService;
    private final UsuarioDao usuarioDao;
    @Autowired
    public ClienteService(ClienteDao clienteDao, AuthService authService, ClienteMapper mapper, UsuarioDao usuarioDao) {
        this.clienteDao = clienteDao;
        this.mapper = mapper;
        this.authService = authService;
        this.usuarioDao = usuarioDao;
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
                // Si el cliente existe, pero está inactivo, activarlo
                clienteByCedula.setActive(true);
                clienteDao.save(clienteByCedula);
                return mapper.toDto(clienteByCedula);
            }
        }

        // Verificar si se proporciona un correo electrónico
        if (clienteDto.getEmail() != null) {
            // Validar el email del cliente
            validateEmail(clienteDto.getEmail());

            // Verificar si ya existe un cliente con el mismo email
            Optional<ClienteBean> existingClientByEmail = clienteDao.findByEmail(clienteDto.getEmail());
            if (existingClientByEmail.isPresent()) {
                ClienteBean clienteByEmail = existingClientByEmail.get();
                if (clienteByEmail.isActive()) {
                    throw new BadRequestException("Ya existe un cliente activo con el mismo email");
                } else if (!clienteByEmail.getCedula().equals(clienteDto.getCedula())) {
                    // Si el cliente existe, pero está inactivo y tiene una cédula diferente, permitir crear un nuevo cliente
                    ClienteBean nuevoCliente = mapper.toBean(clienteDto);
                    nuevoCliente.setActive(true);
                    nuevoCliente.setFechaRegistro(LocalDate.now());
                    clienteDao.save(nuevoCliente);
                    return mapper.toDto(nuevoCliente);
                }
                // Si el cliente existe, pero está inactivo y tiene la misma cédula, se comporta como si fuera activo
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

        // Crear nuevo usuario
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setNombre(clienteDto.getNombre());
        usuarioDto.setEmail(clienteDto.getEmail());
        usuarioDto.setPassword(clienteDto.getCedula());
        usuarioDto.setRol_id(2L);
        // Guardar el nuevo Usuario
        authService.register(usuarioDto);

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

        var clientesDto = clientes.map(mapper::toDto);

        return new PageResponse<>(clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
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
            if (clienteDto.getEmail() != null) {
                // Validar el email del cliente
                validateEmail(clienteDto.getEmail());
                clienteBean.setEmail(clienteDto.getEmail());
            }
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

        var clientesDto = clientes.map(mapper::toDto);
        return new PageResponse<>(
                clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
    }


    @Override
    public PageResponse<ClienteDto> searchByCi(String ci, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findByCedulaAndActiveIsTrue(pag, ci);

        if(clientes.isEmpty()){
            throw new NotFoundException("No hay clientes con esa cedula");
        }
        var clientesDto = clientes.map(mapper::toDto);

        return new PageResponse<>(
                clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
    }

    @Override
    public PageResponse<ClienteDto> searchByRuc(String ruc, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clientes = clienteDao.findAllByRucAndActiveTrue(ruc,pag);

        if(clientes.isEmpty()){
            throw new NotFoundException("No hay clientes con ese ruc");
        }
        var clientesDto = clientes.map(mapper::toDto);

        return new PageResponse<>(
                clientesDto.getContent(),
                clientesDto.getTotalPages(),
                clientesDto.getTotalElements(),
                clientesDto.getNumber() + 1);
    }

    public String createAccountsForClientsWithoutUsuario() {
        //para cada cliente que esté registrado, pero no tenga un usuario con el mismo email, crear un usuario
        var clients = clienteDao.findAllActiveClientsList();
        int conteo = 0;
        for (ClienteBean client : clients) {
            if (client.getEmail() != null) {
                Optional<UsuarioBean> usuario = usuarioDao.findByEmailAndActiveIsTrue(client.getEmail());
                if (usuario.isEmpty()) {
                    System.out.println("Creando usuario para el cliente: " + client.getNombre());
                    UsuarioDto usuarioDto = new UsuarioDto();
                    usuarioDto.setNombre(client.getNombre());
                    usuarioDto.setEmail(client.getEmail());
                    usuarioDto.setPassword(client.getCedula());
                    usuarioDto.setRol_id(2L);
                    // Guardar el nuevo Usuario
                    authService.register(usuarioDto);
                    conteo++;
                //} else {
                //    System.out.println("El cliente " + client.getNombre() + " ya tiene un usuario registrado\n");
                }
            }
        }
        return "Se crearon " + conteo + " cuentas de usuario para los clientes sin usuario";
    }

    public ClienteDto getByEmail(String email){
        var cliente = clienteDao.findByEmailAndActiveTrue(email);
        if(cliente.isEmpty()) throw new NotFoundException("No existe un cliente con ese email");

        return mapper.toDto(cliente.get());

    }
    private void validateEmail(String email) {
        if (email == null) throw new BadRequestException("El email no puede ser nulo");

        // Expresión regular para validar el email según las especificaciones
        //El email debe tener al menos 4 letras antes del @, debe tener un
        String regex = "^[a-zA-Z0-9]{4,}@[a-zA-Z0-9]+\\.(com|net|org|edu|gov|int|mil|arpa|py|ar|[a-zA-Z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            throw new BadRequestException("El email debe tener al menos 4 letras o números antes del arroba, seguido de un dominio conocido y un sufijo opcional");
        }
    }

}
