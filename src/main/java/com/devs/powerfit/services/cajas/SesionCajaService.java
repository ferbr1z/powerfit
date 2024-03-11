package com.devs.powerfit.services.cajas;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SesionCajaService implements ISesionCajaService {
    private SesionCajaMapper sesionCajaMapper;
    private SesionCajaDao sesionCajaDao;
    private CajaDao cajaDao;
    private UsuarioDao usuarioDao;
    @Autowired
    public SesionCajaService(SesionCajaMapper mapper, SesionCajaDao sesionCajaDao, CajaDao cajaDao, UsuarioDao usuarioDao) {
        this.cajaDao = cajaDao;
        this.usuarioDao = usuarioDao;
        this.sesionCajaMapper = mapper;
        this.sesionCajaDao = sesionCajaDao;
    }

    @Override
    public SesionCajaDto create(SesionCajaDto sesionCajaDto) {
        // Verificar si el monto inicial es válido (mayor o igual a 0)
        if (sesionCajaDto.getMontoInicial() < 0) {
            throw new BadRequestException("El monto inicial no puede ser negativo.");
        }

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
        sesionCaja.setCaja(cajaDao.findByIdAndActiveTrue(sesionCajaDto.getIdCaja())
                .orElseThrow(() -> new NotFoundException("Caja no encontrada")));
        sesionCaja.setUsuario(usuarioDao.findByIdAndActiveTrue(sesionCajaDto.getIdUsuario())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado")));
        sesionCaja.setMontoInicial(sesionCajaDto.getMontoInicial());

        // Formatear cadena de fecha a objeto Date
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd-MM-yyyy");
        try {
            String fechaFormateada = sdfFecha.format(sesionCajaDto.getFecha());
            sesionCaja.setFecha(sdfFecha.parse(fechaFormateada));
        } catch (ParseException e) {
            throw new BadRequestException("Formato de fecha inválido: " + sesionCajaDto.getFecha());
        }

        // Formatear cadena de hora a objeto Date
        SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm:ss");
        try {
            String horaAperturaFormateada = sdfHora.format(sesionCajaDto.getHoraApertura());
            sesionCaja.setHoraApertura(sdfHora.parse(horaAperturaFormateada));
        } catch (ParseException e) {
            throw new BadRequestException("Formato de hora de apertura inválido: " + sesionCajaDto.getHoraApertura());
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

        // Mapear las sesiones de caja a DTOs y devolver la respuesta de página
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

        // Verificar si el monto inicial es válido (mayor o igual a 0)
        if (sesionCajaDto.getMontoInicial() < 0) {
            throw new BadRequestException("El monto inicial no puede ser negativo.");
        }

        // Verificar si la fecha y la hora de apertura son válidas (no nulas)
        if (sesionCajaDto.getFecha() == null || sesionCajaDto.getHoraApertura() == null) {
            throw new BadRequestException("La fecha y la hora de apertura son obligatorias.");
        }

        // Verificar si la caja y el usuario están presentes
        if (sesionCajaDto.getIdCaja() == null || sesionCajaDto.getIdUsuario() == null) {
            throw new BadRequestException("La caja y el usuario son obligatorios.");
        }

        // Actualizar los campos de la sesión de caja existente con los valores proporcionados en sesionCajaDto
        sesionCajaExistente.setMontoInicial(sesionCajaDto.getMontoInicial());
        sesionCajaExistente.setMontoFinal(sesionCajaDto.getMontoFinal());
        sesionCajaExistente.setHoraCierre(sesionCajaDto.getHoraCierre());

        // Formatear cadena de fecha a objeto Date
        SimpleDateFormat sdfFecha = new SimpleDateFormat("dd-MM-yyyy");
        try {
            String fechaFormateada = sdfFecha.format(sesionCajaDto.getFecha());
            sesionCajaExistente.setFecha(sdfFecha.parse(fechaFormateada));
        } catch (ParseException e) {
            throw new BadRequestException("Formato de fecha inválido: " + sesionCajaDto.getFecha());
        }

        // Formatear cadena de hora de apertura a objeto Date
        SimpleDateFormat sdfHoraApertura = new SimpleDateFormat("HH:mm:ss");
        try {
            String horaAperturaFormateada = sdfHoraApertura.format(sesionCajaDto.getHoraApertura());
            sesionCajaExistente.setHoraApertura(sdfHoraApertura.parse(horaAperturaFormateada));
        } catch (ParseException e) {
            throw new BadRequestException("Formato de hora de apertura inválido: " + sesionCajaDto.getHoraApertura());
        }

        // Formatear cadena de hora de cierre a objeto Date
        SimpleDateFormat sdfHoraCierre = new SimpleDateFormat("HH:mm:ss");
        try {
            String horaCierreFormateada = sdfHoraCierre.format(sesionCajaDto.getHoraCierre());
            sesionCajaExistente.setHoraCierre(sdfHoraCierre.parse(horaCierreFormateada));
        } catch (ParseException e) {
            throw new BadRequestException("Formato de hora de cierre inválido: " + sesionCajaDto.getHoraCierre());
        }

        // Guardar la sesión de caja actualizada
        SesionCajaBean sesionCajaActualizada = sesionCajaDao.save(sesionCajaExistente);

        return sesionCajaMapper.toDto(sesionCajaActualizada);
    }


    @Override
    public boolean delete(Long id) {
        var caja = sesionCajaDao.findByIdAndActiveTrue(id);
        if (caja.isPresent()) {
            var sesionCajaBean = caja.get();
           sesionCajaBean.setActive(false);
            sesionCajaDao.save(sesionCajaBean);
            return true;
        }
        throw new NotFoundException("Caja no encontrada");
    }
}