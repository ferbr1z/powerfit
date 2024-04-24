package com.devs.powerfit.services.cajas;

import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.daos.cajas.CajaDao;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.dtos.cajas.SesionCajaDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.cajas.ISesionCajaService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.CajaMappers.SesionCajaMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@Transactional
public class SesionCajaService implements ISesionCajaService {
    private final SesionCajaMapper sesionCajaMapper;
    private final SesionCajaDao sesionCajaDao;
    private final CajaDao cajaDao;
    private final UsuarioDao usuarioDao;

    @Autowired
    public SesionCajaService(SesionCajaMapper mapper, SesionCajaDao sesionCajaDao, CajaDao cajaDao, UsuarioDao usuarioDao) {
        this.cajaDao = cajaDao;
        this.sesionCajaMapper = mapper;
        this.sesionCajaDao = sesionCajaDao;
        this.usuarioDao = usuarioDao;
    }

    @Override
    public SesionCajaDto create(SesionCajaDto sesionCajaDto) {

        // Verificar si la fecha y la hora de apertura son válidas (no nulas)
        if (sesionCajaDto.getFecha() == null || sesionCajaDto.getHoraApertura() == null) {
            throw new BadRequestException("La fecha y la hora de apertura son obligatorias.");
        }

        // Verificar si la caja y el usuario están presentes
        if (sesionCajaDto.getIdCaja() == null || sesionCajaDto.getIdUsuario() == null) {
            throw new BadRequestException("La caja y el usuario son obligatorios.");
        }

        // Setear el active a true antes de guardar
        sesionCajaDto.setActive(true);

        // Realizar la apertura de la caja
        SesionCajaBean sesionCaja = new SesionCajaBean();
        sesionCaja.setActive(true);
        var caja = cajaDao.findByIdAndActiveTrue(sesionCajaDto.getIdCaja());
        if (caja.isEmpty()) {
            throw new NotFoundException("No se encontró caja con ese id");
        }
        CajaBean cajaExistente = caja.get();
        sesionCaja.setCaja(cajaExistente);
        var usuario = usuarioDao.findByIdAndActiveTrue(sesionCajaDto.getIdUsuario());
        if (usuario.isEmpty()) {
            throw new NotFoundException("No se encontró usuario con ese id");
        }
        var usuarioExistente = usuario.get();
        sesionCaja.setUsuario(usuarioExistente);
        // Obtener el monto actual de la caja
        double montoCaja = cajaExistente.getMonto();
        sesionCaja.setMontoInicial(montoCaja);

        // Parsear cadena de fecha a LocalDate o establecer en la fecha actual
        if (sesionCajaDto.getFecha() != null) {
            LocalDate fechaFormateada = sesionCajaDto.getFecha();
            sesionCaja.setFecha(fechaFormateada);

        } else {
            sesionCaja.setFecha(LocalDate.now());
        }

// Parsear cadena de hora a LocalTime o establecer en la hora actual
        if (sesionCajaDto.getHoraApertura() != null) {
            LocalTime horaAperturaFormateada = sesionCajaDto.getHoraApertura();
            sesionCaja.setHoraApertura(horaAperturaFormateada);
        } else {
            sesionCaja.setHoraApertura(LocalTime.now());
        }

        // Guardar la sesión de caja
        sesionCajaDao.save(sesionCaja);

        return sesionCajaMapper.toDto(sesionCaja);
    }


    @Override
    public SesionCajaDto getById(Long id) {
        // Buscar la sesión de caja por su ID en la base de datos
        Optional<SesionCajaBean> sesionCajaOptional = sesionCajaDao.findByIdAndActiveTrue(id);

        // Verificar si la sesión de caja fue encontrada
        if (sesionCajaOptional.isPresent()) {
            // Mapear la sesión de caja a un DTO y devolverlo
            return sesionCajaMapper.toDto(sesionCajaOptional.get());
        } else {
            // Si la sesión de caja no fue encontrada, lanzar una excepción NotFoundException
            throw new NotFoundException("Sesión de caja no encontrada con el ID proporcionado: " + id);
        }
    }


    @Override
    public PageResponse<SesionCajaDto> getAll(int page) {
        // Definir la paginación
        var pageable = PageRequest.of(page - 1, Setting.PAGE_SIZE);

        // Obtener la página de sesiones de caja de la base de datos
        var sesionesCaja = sesionCajaDao.findAllByActiveTrue(pageable);

        // Verificar si hay sesiones de caja en la página
        if (sesionesCaja.isEmpty()) {
            throw new NotFoundException("No hay sesiones de caja en la lista");
        }

        // Mapear las sesiones de caja a DTO y devolver la respuesta de página
        var sesionesCajaDto = sesionesCaja.map(sesionCajaMapper::toDto);
        return new PageResponse<>(
                sesionesCajaDto.getContent(),
                sesionesCaja.getTotalPages(),
                sesionesCaja.getTotalElements(),
                sesionesCaja.getNumber() + 1
        );
    }


    @Override
    public SesionCajaDto update(Long id, SesionCajaDto sesionCajaDto) {
        // Verificar si la sesión de caja con el ID proporcionado existe en la base de datos
        SesionCajaBean sesionCajaExistente = sesionCajaDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Sesión de caja no encontrada con el ID proporcionado: " + id));

        // Verificar si la sesión de caja ya ha sido cerrada
        if (sesionCajaExistente.getHoraCierre() != null) {
            throw new BadRequestException("La sesión de caja ya ha sido cerrada y no puede ser actualizada.");
        }

        // Obtener la caja del DTO si está presente y establecerla en la sesión de caja existente
        if (sesionCajaDto.getIdCaja() != null) {
            var caja = cajaDao.findByIdAndActiveTrue(sesionCajaDto.getIdCaja())
                    .orElseThrow(() -> new NotFoundException("Caja no encontrada con el ID proporcionado: " + sesionCajaDto.getIdCaja()));
            sesionCajaExistente.setCaja(caja);
        }

        // Obtener el usuario del DTO si está presente y establecerlo en la sesión de caja existente
        if (sesionCajaDto.getIdUsuario() != null) {
            var usuario = usuarioDao.findByIdAndActiveTrue(sesionCajaDto.getIdUsuario())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado con el ID proporcionado: " + sesionCajaDto.getIdUsuario()));
            sesionCajaExistente.setUsuario(usuario);
        }

        // Actualizar la hora de cierre si se proporciona en el DTO
        if (sesionCajaDto.getHoraCierre() != null) {
            sesionCajaExistente.setHoraCierre(sesionCajaDto.getHoraCierre());
        }

// Actualizar la fecha si se proporciona en el DTO
        if (sesionCajaDto.getFecha() != null) {
            // Verificar si la fecha proporcionada tiene el formato correcto
            LocalDate fechaFormateada =sesionCajaDto.getFecha();
            sesionCajaExistente.setFecha(fechaFormateada);
        }

// Actualizar la hora de apertura si se proporciona en el DTO
        if (sesionCajaDto.getHoraApertura() != null) {
            // Verificar si la hora de apertura proporcionada tiene el formato correcto
            try {
                LocalTime horaAperturaFormateada = sesionCajaDto.getHoraApertura();
                sesionCajaExistente.setHoraApertura(horaAperturaFormateada);
            } catch (DateTimeParseException e) {
                throw new BadRequestException("Formato de hora de apertura inválido: " + sesionCajaDto.getHoraApertura());
            }
        }
        sesionCajaExistente.setMontoFinal(sesionCajaExistente.getCaja().getMonto());
        SesionCajaBean sesionCajaActualizada = sesionCajaDao.save(sesionCajaExistente);


        return sesionCajaMapper.toDto(sesionCajaActualizada);
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    public boolean aumentarMontoCaja(Long id, Double monto) {
        Optional<SesionCajaBean> sesionCajaExistente = sesionCajaDao.findById(id);
        if (sesionCajaExistente.isEmpty()) {
            return false;
        }
        var caja = sesionCajaExistente.get().getCaja();
        caja.setMonto(caja.getMonto() + monto);
        cajaDao.save(caja);
        return true;

    }

    public boolean disminuirMontoCaja(Long id, Double monto) {
        Optional<SesionCajaBean> sesionCajaExistente = sesionCajaDao.findById(id);
        if (sesionCajaExistente.isEmpty()) {
            return false;
        }
        var caja = sesionCajaExistente.get().getCaja();
        caja.setMonto(caja.getMonto() - monto);
        cajaDao.save(caja);
        return true;

    }
    public PageResponse<SesionCajaDto> searchByFecha(int page, LocalDate fechaInicio, LocalDate fechaFin) {
        // Validar que la fecha final sea igual o posterior a la fecha inicial
        if (fechaFin.isBefore(fechaInicio)) {
            throw new BadRequestException("La fecha final debe ser igual o posterior a la fecha inicial");
        }

        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var sesionCajaPage = sesionCajaDao.findAllByFechaBetweenAndActiveTrue(pageRequest, fechaInicio, fechaFin);
        if (sesionCajaPage.isEmpty()) {
            throw new NotFoundException("No hay sesiones de caja en la lista");
        }
        var sesionCajaDtoPage = sesionCajaPage.map(sesionCajaMapper::toDto);
        return new PageResponse<>(sesionCajaDtoPage.getContent(),
                sesionCajaDtoPage.getTotalPages(),
                sesionCajaDtoPage.getTotalElements(),
                sesionCajaDtoPage.getNumber() + 1);
    }


}
