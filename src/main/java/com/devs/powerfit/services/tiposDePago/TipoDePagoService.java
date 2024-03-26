package com.devs.powerfit.services.tiposDePago;

import com.devs.powerfit.beans.tipoDePagos.TipoDePagoBean;
import com.devs.powerfit.daos.tiposDePago.TipoDePagoDao;
import com.devs.powerfit.dtos.tiposDePagos.TipoDePagoDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.tiposDePago.ITipoDePagoService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.tiposDePagoMapper.TipoDePagoMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TipoDePagoService implements ITipoDePagoService {
    private final TipoDePagoDao dao;
    private final TipoDePagoMapper mapper;
    @Autowired
    public TipoDePagoService(TipoDePagoDao dao, TipoDePagoMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;
    }
    @Override
    public TipoDePagoDto create(TipoDePagoDto tipoDePagoDto) {
        var tipoDePago = mapper.toBean(tipoDePagoDto);
        Optional<TipoDePagoBean> existente = dao.findByNombreContainingIgnoreCase(tipoDePago.getNombre());
        if (!existente.isEmpty()){
            if(existente.get().isActive()){
                throw new BadRequestException("Ya existe un tipo de pago activo con el mismo nombre");
            }else {
                var inactivo =existente.get();
                inactivo.setDescripcion(tipoDePagoDto.getDescripcion());
                inactivo.setActive(true);
                dao.save(inactivo);
                return  mapper.toDto(inactivo);
            }
        }
        tipoDePago.setActive(true);
        dao.save(tipoDePago);
        return mapper.toDto(tipoDePago);
    }

    @Override
    public TipoDePagoDto getById(Long id) {
        var tipoDePago = dao.findByIdAndActiveTrue(id);
        if (tipoDePago.isPresent()) {
            return mapper.toDto(tipoDePago.get());
        }
        throw new NotFoundException("tipoDePago no encontrado");

    }

    @Override
    public PageResponse<TipoDePagoDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var tiposDePago = dao.findAllByActiveTrue(pag);
        if (tiposDePago.isEmpty()) {
            throw new NotFoundException("No hay tiposDePago en la lista");
        }
        var tiposDePagoDto = tiposDePago.map(mapper::toDto);
        return new PageResponse<>(tiposDePagoDto.getContent(),
                tiposDePagoDto.getTotalPages(),
                tiposDePagoDto.getTotalElements(),
                tiposDePagoDto.getNumber() + 1);
    }

    @Override
    public TipoDePagoDto update(Long id, TipoDePagoDto tipoDePagoDto) {
        var tipoDePago = dao.findByIdAndActiveTrue(id);
        var existente = dao.findByNombreContainingIgnoreCase(tipoDePagoDto.getNombre());

        if (tipoDePago.isPresent()) {
            var tipoDePagoBean = tipoDePago.get();

            // Verificar si se proporcionó un nuevo nombre y si es diferente al nombre existente
            if (tipoDePagoDto.getNombre() != null && !tipoDePagoDto.getNombre().equalsIgnoreCase(tipoDePagoBean.getNombre())) {
                // Verificar si ya existe un registro con el nuevo nombre
                if (!existente.isPresent()) {
                    // Si existe pero está inactivo, activarlo y actualizar el nombre
                    if (!existente.get().isActive()) {
                        var existenteBean =existente.get();
                        existenteBean.setActive(true);
                        existenteBean.setDescripcion(tipoDePagoDto.getDescripcion()); // Establecer la nueva descripción
                        dao.save(existenteBean);
                        return mapper.toDto(existenteBean);
                    } else {
                        throw new RuntimeException("Ya existe un tipo de pago con el nombre proporcionado");
                    }
                }
                // Actualizar el nombre si es diferente y no existe un registro con ese nombre
                tipoDePagoBean.setNombre(tipoDePagoDto.getNombre());
            }

            // Actualizar la descripción si se proporciona
            if (tipoDePagoDto.getDescripcion() != null) {
                tipoDePagoBean.setDescripcion(tipoDePagoDto.getDescripcion());
            }

            dao.save(tipoDePagoBean);
            return mapper.toDto(tipoDePagoBean);
        }
        throw new NotFoundException("Tipo de pago no encontrado");
    }

    @Override
    public boolean delete(Long id) {
        var tipoDePago = dao.findByIdAndActiveTrue(id);
        if (tipoDePago.isPresent()) {
            var tipoDePagoBean = tipoDePago.get();
            tipoDePagoBean.setActive(false);
            dao.save(tipoDePagoBean);
            return true;
        }
        throw new NotFoundException("tipoDePago no encontrada");
    }

    @Override
    public PageResponse<TipoDePagoDto> searchByNombre(String nombre, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var tiposDePago = dao.findAllByNombreContainingIgnoreCaseAndActiveTrue(pag, nombre);
        if (tiposDePago.isEmpty()) {
            throw new NotFoundException("No hay tiposDePago en la lista");
        }
        var tiposDePagoDto = tiposDePago.map(mapper::toDto);
        return new PageResponse<>(
                tiposDePagoDto.getContent(),
                tiposDePagoDto.getTotalPages(),
                tiposDePagoDto.getTotalElements(),
                tiposDePagoDto.getNumber() + 1);
    }
}
