package com.devs.powerfit.services.proveedores;

import com.devs.powerfit.beans.proveedores.ProveedorBean;
import com.devs.powerfit.daos.proveedores.ProveedorDao;
import com.devs.powerfit.dtos.proveedores.ProveedorDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.proveedores.IProveedorService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.proveedorMapper.ProveedorMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProveedorService implements IProveedorService {

    private ProveedorDao proveedorDao;
    private ProveedorMapper mapper;

    @Autowired
    public ProveedorService(ProveedorDao proveedorDao, ProveedorMapper mapper){
        this.proveedorDao = proveedorDao;
        this.mapper = mapper;
    }
    @Override
    public ProveedorDto create(ProveedorDto proveedorDto) {
        if (proveedorDao.findByEmailAndActiveIsTrue(proveedorDto.getEmail()).isPresent()){
            throw new BadRequestException("Ya existe un proovedor activo con ese email");
        }
        if (proveedorDao.findByRucAndActiveIsTrue(proveedorDto.getRuc()).isPresent()){
            throw new BadRequestException("Ya existe un proovedor activo con ese Ruc");
        }
        ProveedorBean proveedorBean = mapper.toBean(proveedorDto);
        proveedorBean.setActive(true);
        proveedorDao.save(proveedorBean);
        return mapper.toDto(proveedorBean);
    }

    @Override
    public ProveedorDto getById(Long id) {
        Optional<ProveedorBean> proveedor = proveedorDao.findByIdAndActiveIsTrue(id);
        if(proveedor.isPresent()){
            return mapper.toDto(proveedor.get());
        }
        throw new NotFoundException("Proveedor no encontrado");
    }

    @Override
    public PageResponse<ProveedorDto> getAll(int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<ProveedorBean> proveedores = proveedorDao.findAll(pag);
        if (proveedores.isEmpty()){
            throw new NotFoundException("No hay proveedores en la lista");
        }
        var proveedoresDto = proveedores.map(proveedor -> mapper.toDto(proveedor));

        return new PageResponse<ProveedorDto>(proveedoresDto.getContent(),
                proveedoresDto.getTotalPages(),
                proveedoresDto.getTotalElements(),
                proveedoresDto.getNumber() + 1);
    }

    @Override
    public ProveedorDto update(Long id, ProveedorDto proveedorDto) {
        var proveedorOptional = proveedorDao.findByIdAndActiveIsTrue(id);
        if (proveedorOptional.isPresent()) {
            ProveedorBean proveedor = proveedorOptional.get();
            if (proveedorDto.getNombre() != null) proveedor.setNombre(proveedorDto.getNombre());
            if (proveedorDto.getEmail() != null) proveedor.setEmail(proveedorDto.getEmail());
            if (proveedorDto.getRuc() != null) proveedor.setRuc(proveedorDto.getRuc());
            if (proveedorDto.getTelefono() != null) proveedor.setTelefono(proveedorDto.getTelefono());
            if (proveedorDto.getDireccion() != null) proveedor.setDireccion(proveedorDto.getDireccion());

            // Verificar si la actualización tiene éxito
            ProveedorBean updatedProveedor = proveedorDao.save(proveedor);
            if (updatedProveedor != null) {
                // Se ha actualizado correctamente
                return mapper.toDto(updatedProveedor); // Método para convertir ProveedorBean a ProveedorDto
            } else {
                throw new RuntimeException("Error al actualizar el proveedor");
            }
        } else {
            throw new NotFoundException("Proveedor no encontrado");
        }
    }

    @Override
    public boolean delete(Long id) {
        Optional<ProveedorBean> proveedor = proveedorDao.findByIdAndActiveIsTrue(id);
        if (proveedor.isPresent()){
            ProveedorBean bean = proveedor.get();
            bean.setActive(false);
            proveedorDao.save(bean);
            return true;
        }
        throw new NotFoundException("Proveedor no encontrado");
    }
}
