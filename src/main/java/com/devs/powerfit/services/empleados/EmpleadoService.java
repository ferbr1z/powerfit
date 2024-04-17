package com.devs.powerfit.services.empleados;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.empleados.EmpleadoBean;
import com.devs.powerfit.daos.auth.RolDao;
import com.devs.powerfit.daos.empleados.EmpleadoDao;
import com.devs.powerfit.dtos.auth.UsuarioDto;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.empleados.IEmpleadoService;
import com.devs.powerfit.services.auth.AuthService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.empleadoMappers.EmpleadoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class EmpleadoService implements IEmpleadoService {

    private EmpleadoDao empleadoDao;
    private EmpleadoMapper mapper;

    private RolDao rolDao;

    private AuthService authService;
    @Autowired
    public EmpleadoService(EmpleadoDao empleadoDao, EmpleadoMapper mapper, AuthService authService,RolDao rolDao ){
        this.empleadoDao = empleadoDao;
        this.mapper = mapper;
        this.authService = authService;
        this.rolDao = rolDao;
    }
    @Override
    public EmpleadoDto create(EmpleadoDto empleadoDto) {

        if (empleadoDao.findByEmailAndActiveIsTrue(empleadoDto.getEmail()).isPresent()){
            throw new BadRequestException("Ya existe un empleado activo con el mismo email");
        }
        if (empleadoDao.findByCedulaAndActiveIsTrue(empleadoDto.getCedula()).isPresent()){
            throw new BadRequestException("Ya existe un empleado activo con el mismo numero de cédula");
        }

        EmpleadoBean empleadoBean = mapper.toBean(empleadoDto);
        empleadoBean.setActive(true);
        empleadoDao.save(empleadoBean);

        // Crear un nuevo Usuario con los datos del Empleado
        UsuarioDto usuarioDto = new UsuarioDto();
        usuarioDto.setNombre(empleadoDto.getNombre());
        usuarioDto.setEmail(empleadoDto.getEmail());
        usuarioDto.setPassword(empleadoDto.getCedula()); // La contraseña es la cedula del empleado
        usuarioDto.setRol_id(empleadoDto.getRol());
        // Guardar el nuevo Usuario
        authService.register(usuarioDto);

        return mapper.toDto(empleadoBean);
    }

    @Override
    public EmpleadoDto getById(Long id) {
        Optional<EmpleadoBean> empleado = empleadoDao.findByIdAndActiveIsTrue(id);
        if(empleado.isPresent()){
            return mapper.toDto(empleado.get());
        }else {
            throw new NotFoundException("Empleado no encontrado");
        }
    }

    @Override
    public PageResponse<EmpleadoDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<EmpleadoBean> empleados = empleadoDao.findAllByActiveIsTrue(pag);
        if (empleados.isEmpty()){
            throw new NotFoundException("No hay empleados en la lista");
        }
        var empleadosDto = empleados.map(empleado -> mapper.toDto(empleado));
        return new PageResponse<EmpleadoDto>(empleadosDto.getContent(),
                empleadosDto.getTotalPages(),
                empleadosDto.getTotalElements(),
                empleadosDto.getNumber() + 1);
    }

    @Override
    public EmpleadoDto update(Long id, EmpleadoDto empleadoDto) {
        Optional<EmpleadoBean> empleadoOptional = empleadoDao.findByIdAndActiveIsTrue(id);
        if (empleadoOptional.isPresent()) {
            UsuarioDto usuarioDto = new UsuarioDto();
            EmpleadoBean empleadoBean = empleadoOptional.get();
            UsuarioBean usuario = authService.getByEmail(empleadoBean.getEmail());
            if (empleadoDto.getNombre() != null) {
                empleadoBean.setNombre(empleadoDto.getNombre());
                usuarioDto.setNombre(empleadoDto.getNombre());
            }
            if (empleadoDto.getCedula() != null && !empleadoDto.getCedula().equals(empleadoBean.getCedula())) {
                // Verificar si la nueva cédula ya está en uso por otro empleado
                if (empleadoDao.findByCedulaAndActiveIsTrue(empleadoDto.getCedula()).isPresent()) {
                    throw new BadRequestException("Ya existe un empleado activo con la misma cédula");
                }
                empleadoBean.setCedula(empleadoDto.getCedula());
                usuarioDto.setPassword(empleadoDto.getCedula()); // La contraseña es la cédula del empleado
            }

            if (empleadoDto.getRol() != null) {
                if (!rolDao.findByIdAndActiveTrue(empleadoDto.getRol()).isPresent()) {
                    throw new BadRequestException("El rol especificado no existe");
                }
                empleadoBean.setRol(empleadoDto.getRol());
                usuarioDto.setRol_id(empleadoDto.getRol());
            }
            if (empleadoDto.getDireccion() != null) empleadoBean.setDireccion(empleadoDto.getDireccion());
            if (empleadoDto.getEmail() != null && !empleadoDto.getEmail().equals(empleadoBean.getEmail())) {
                // Verificar si el nuevo email ya está en uso por otro empleado
                if (empleadoDao.findByEmailAndActiveIsTrue(empleadoDto.getEmail()).isPresent()) {
                    throw new BadRequestException("Ya existe un empleado activo con el mismo email");
                }
                usuarioDto.setEmail(empleadoDto.getEmail());
                empleadoBean.setEmail(empleadoDto.getEmail());
            }
            if (empleadoDto.getTelefono() != null) empleadoBean.setTelefono(empleadoDto.getTelefono());
            // Actualizar los datos del usuario asociado al empleado
            authService.update(usuario.getId(), usuarioDto); // Actualizar usuario
            empleadoDao.save(empleadoBean);

            return mapper.toDto(empleadoBean);
        }
        throw new NotFoundException("Empleado no encontrado.");
    }

    @Override
    public boolean delete(Long id) {
        Optional<EmpleadoBean> empleado = empleadoDao.findByIdAndActiveIsTrue(id);
        if(empleado.isPresent()){
            EmpleadoBean empleadoBean = empleado.get();
            empleadoBean.setActive(false);
            empleadoDao.save(empleadoBean);

            // Eliminar el usuario asociado al empleado
            authService.delete(empleadoBean.getEmail());

            return true;
        }
        throw new NotFoundException("Empleado no encontrado.");
    }

    @Override
    public PageResponse<EmpleadoDto> searchByNombre(String nombre, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<EmpleadoBean> empleados = empleadoDao.findAllByNombreContainingIgnoreCaseAndActiveIsTrue(pag,nombre);
        if (empleados.isEmpty()){
            throw new NotFoundException("No hay empleados en la lista");
        }
        var empleadosDto = empleados.map(empleado -> mapper.toDto(empleado));
        return new PageResponse<EmpleadoDto>(empleadosDto.getContent(),
                empleadosDto.getTotalPages(),
                empleadosDto.getTotalElements(),
                empleadosDto.getNumber() + 1);
    }

    @Override
    public PageResponse<EmpleadoDto> searchByRolId(Long id, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<EmpleadoBean> empleados = empleadoDao.findAllByRolAndActiveIsTrue(pag,id);
        if (empleados.isEmpty()){
            throw new NotFoundException("No hay empleados en la lista");
        }
        var empleadosDto = empleados.map(empleado -> mapper.toDto(empleado));
        return new PageResponse<EmpleadoDto>(empleadosDto.getContent(),
                empleadosDto.getTotalPages(),
                empleadosDto.getTotalElements(),
                empleadosDto.getNumber() + 1);
    }

    @Override
    public EmpleadoDto getByEmail(String email) {
        Optional<EmpleadoBean> empleado = empleadoDao.findByEmailAndActiveIsTrue(email);
        if(empleado.isPresent()){
            return mapper.toDto(empleado.get());
        }else {
            throw new NotFoundException("Empleado no encontrado");
        }
    }


    @Override
    public PageResponse<EmpleadoDto> getAllEntrenadores(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Long rolEntrenador = 4L;
        Page<EmpleadoBean> empleados = empleadoDao.findAllByRolAndActiveIsTrue(pag, rolEntrenador);
        if (empleados.isEmpty()){
            throw new NotFoundException("No hay empleados en la lista");
        }
        var empleadosDto = empleados.map(empleado -> mapper.toDto(empleado));
        return new PageResponse<EmpleadoDto>(empleadosDto.getContent(),
                empleadosDto.getTotalPages(),
                empleadosDto.getTotalElements(),
                empleadosDto.getNumber() + 1);
    }
}
