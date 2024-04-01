package com.devs.powerfit.services.cajas;

import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.daos.cajas.CajaDao;
import com.devs.powerfit.dtos.actividades.ActividadDto;
import com.devs.powerfit.dtos.cajas.CajaDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.cajas.ICajaService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.CajaMappers.CajaMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CajaService implements ICajaService {
    private final CajaDao cajaDao;
    private final CajaMapper mapper;

    @Autowired
    public CajaService(CajaDao cajaDao, CajaMapper mapper) {
        this.cajaDao = cajaDao;
        this.mapper = mapper;
    }

    @Override
    public CajaDto create(CajaDto cajaDto) {
        // Verificar si el nombre de la caja está presente
        if (cajaDto.getNombre() == null || cajaDto.getNombre().isEmpty()) {
            throw new BadRequestException("El nombre de la caja no puede estar vacío.");
        }
        // Verificar si el monto es válido (mayor o igual a 0)
        if (cajaDto.getMonto() < 0) {
            throw new BadRequestException("El monto de la caja no puede ser negativo.");
        }

        // Buscar la última caja creada
        Optional<CajaBean> lastCajaOptional = cajaDao.findFirstByOrderByNumeroCajaDesc();

        // Crear la nueva caja
        CajaBean caja = new CajaBean();
        caja.setNombre(cajaDto.getNombre());
        caja.setMonto(cajaDto.getMonto());
        caja.setActive(true);

        // Asignar número de caja
        if (lastCajaOptional.isPresent()) {
            // Si hay cajas existentes, obtener el número de caja de la última caja y aumentarlo en 1
            Long lastNumeroCaja = lastCajaOptional.get().getNumeroCaja();
            caja.setNumeroCaja(lastNumeroCaja + 1);
        } else {
            // Si no hay cajas existentes, asignar el número de caja como 1
            caja.setNumeroCaja(1L);
        }

        // Guardar la nueva caja en la base de datos
        cajaDao.save(caja);

        return mapper.toDto(caja);
    }

    @Override
    public CajaDto getById(Long id) {
        var caja = cajaDao.findByIdAndActiveTrue(id);
        if (caja.isPresent()) {
            return mapper.toDto(caja.get());
        }
        throw new NotFoundException("Caja no encontrada");
    }

    @Override
    public PageResponse<CajaDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var cajas = cajaDao.findAllByActiveTrue(pag);
        if (cajas.isEmpty()) {
            throw new NotFoundException("No hay cajas en la lista");
        }
        var cajasDto = cajas.map(mapper::toDto);
        return new PageResponse<>(cajasDto.getContent(),
                cajasDto.getTotalPages(),
                cajasDto.getTotalElements(),
                cajasDto.getNumber() + 1);
    }

    @Override
    public CajaDto update(Long id, CajaDto cajaDto) {
        // Verificar si el nombre de la caja está presente
        if (cajaDto.getNombre() == null || cajaDto.getNombre().isEmpty()) {
            throw new BadRequestException("El nombre de la caja no puede estar vacío.");
        }

        // Verificar si el monto es válido (mayor o igual a 0)
        if (cajaDto.getMonto() < 0) {
            throw new BadRequestException("El monto de la caja no puede ser negativo.");
        }

        var caja = cajaDao.findByIdAndActiveTrue(id);
        if (caja.isPresent()) {
            var cajaBean = caja.get();

            // Verificar si se está intentando cambiar el nombre a uno que ya está en uso
            if (!cajaDto.getNombre().equals(cajaBean.getNombre()) && cajaDao.existsByNombreAndActiveTrue(cajaDto.getNombre())) {
                throw new BadRequestException("El nombre de la caja ya está en uso.");
            }

            if (cajaDto.getNombre() != null) cajaBean.setNombre(cajaDto.getNombre());
            if (cajaDto.getMonto() != null) cajaBean.setMonto(cajaDto.getMonto());

            cajaDao.save(cajaBean);

            return mapper.toDto(cajaBean);
        }
        throw new NotFoundException("Caja no encontrada");
    }

    @Override
    public boolean delete(Long id) {
        var caja = cajaDao.findByIdAndActiveTrue(id);
        if (caja.isPresent()) {
            var cajaBean = caja.get();
            cajaBean.setActive(false);
            cajaDao.save(cajaBean);
            return true;
        }
        throw new NotFoundException("Caja no encontrada");
    }
    public PageResponse<CajaDto> searchByNombre(String nombre, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var cajas = cajaDao.findAllByNombreContainingIgnoreCaseAndActiveTrue(pag, nombre);

        if (cajas.isEmpty()) {
            throw new NotFoundException("No hay actividades en la lista");
        }

        var cajasDto = cajas.map(mapper::toDto);
        return new PageResponse<>(
                cajasDto.getContent(),
                cajasDto.getTotalPages(),
                cajasDto.getTotalElements(),
                cajasDto.getNumber() + 1);
    }
}
