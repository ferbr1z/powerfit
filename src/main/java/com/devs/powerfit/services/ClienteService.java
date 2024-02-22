package com.devs.powerfit.services;

import com.devs.powerfit.beans.ClienteBean;
import com.devs.powerfit.daos.ClienteDao;
import com.devs.powerfit.dtos.ClienteDto;
import com.devs.powerfit.dtos.PageResponse;
import com.devs.powerfit.interfaces.IClienteService;
import com.devs.powerfit.interfaces.IMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClienteService implements IClienteService {

    private ClienteDao clienteDao;
    private final IMapper<ClienteBean,ClienteDto> mapper;
    @Autowired
    public ClienteService(ClienteDao clienteDao, IMapper<ClienteBean, ClienteDto> mapper) {
        this.clienteDao = clienteDao;
        this.mapper = mapper;
    }


    @Override
    public ClienteDto create(ClienteDto clienteDto) {
        var cliente = mapper.toBean(clienteDto, ClienteBean.class);
        cliente.setActive(true);
        clienteDao.save(cliente);
        return mapper.toDto(cliente, ClienteDto.class);
    }

    @Override
    public ClienteDto getById(Long id) {
        return null;
    }

    @Override
    public Page<ClienteDto> getAll(Pageable pag) {
        return null;
    }

    @Override
    public ClienteDto update(Long id, ClienteDto clienteDto) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }
    @Override
    public PageResponse<ClienteDto> searchByNombre(String nombre, int page) {
        return null;
    }

    @Override
    public PageResponse<ClienteDto> searchByCi(String ci, int page) {
        return null;
    }
}
