package com.devs.powerfit.services.programas;

import com.devs.powerfit.daos.programas.ClienteProgramaDao;
import com.devs.powerfit.daos.programas.ClienteProgramaItemDao;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaItemDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.programas.IProgramItemService;
import com.devs.powerfit.interfaces.programas.IProgramaClienteItemService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.programaMapper.ClienteProgramaItemMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteProgramaItemService implements IProgramaClienteItemService {

    ClienteProgramaItemMapper _mapper;
    ClienteProgramaItemDao _repository;
    private ClienteProgramaDao _clienteProgramaRepository;
    private IProgramItemService _programItemService;

    @Autowired
    public ClienteProgramaItemService(ClienteProgramaItemMapper mapper, ClienteProgramaItemDao repository, ClienteProgramaDao clienteProgramaRepository, IProgramItemService programItemService) {
        this._mapper = mapper;
        this._repository = repository;
        this._clienteProgramaRepository = clienteProgramaRepository;
        this._programItemService = programItemService;
    }

    @Override
    public ClienteProgramaItemDto create(Long programaId, Long clienteProgramaId, ClienteProgramaItemDto clienteProgramaItemDto) {
        var clienteProgramaBean = _clienteProgramaRepository.findByProgramaIdAndId(programaId, clienteProgramaId);

        if(clienteProgramaBean.isEmpty()) throw new NotFoundException("Registro de cliente no encontrado");
        var programaItemId = clienteProgramaItemDto.getProgramaItemId();

        if(_programItemService.getItemById(programaId, programaItemId)==null) throw new NotFoundException("Item de programa no encontrado");

        clienteProgramaItemDto.setClienteProgramaId(clienteProgramaId);

        var itemBean = _mapper.toBean(clienteProgramaItemDto);
        itemBean.setActive(true);
        var newItemBean = _repository.save(itemBean);
        return _mapper.toDto(newItemBean);
    }

    @Override
    public ClienteProgramaItemDto getById(Long programaId, Long clienteProgramaId, Long id) {
        var itemBean = _repository.findByIdCustom(programaId, clienteProgramaId, id);
        if(itemBean.isEmpty()) return null;
        return _mapper.toDto(itemBean.get());
    }

    @Override
    public PageResponse<ClienteProgramaItemDto> getAll(Long programaId, Long clienteProgramaId, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var items = _repository.findAllCustom(programaId, clienteProgramaId, pag);
        var itemsDto = items.map(_mapper::toDto);
        return new PageResponse<ClienteProgramaItemDto>(
                itemsDto.getContent(),
                itemsDto.getTotalPages(),
                itemsDto.getTotalElements(),
                itemsDto.getNumber() + 1
        );
    }

    @Override
    public ClienteProgramaItemDto update(Long programaId, Long clienteProgramaId, Long id, ClienteProgramaItemDto clienteProgramaItemDto) {
        var itemBean = _repository.findByIdCustom(programaId, clienteProgramaId, id);

        if(itemBean.isEmpty()) return null;

        var itemBeanToUpdate = itemBean.get();

        if( clienteProgramaItemDto.getClienteProgramaId()!=null) itemBeanToUpdate.getClientePrograma().setId(clienteProgramaItemDto.getClienteProgramaId());
        if( clienteProgramaItemDto.getProgramaItemId()!=null) itemBeanToUpdate.getProgramaItem().setId(clienteProgramaItemDto.getProgramaItemId());
        if( clienteProgramaItemDto.getTiempo()!=null) itemBeanToUpdate.setTiempo(clienteProgramaItemDto.getTiempo());
        if( clienteProgramaItemDto.getRepeticiones()!=null) itemBeanToUpdate.setRepeticiones(clienteProgramaItemDto.getRepeticiones());
        if( clienteProgramaItemDto.getPeso()!=null) itemBeanToUpdate.setPeso(clienteProgramaItemDto.getPeso());
        if( clienteProgramaItemDto.getLogrado()!=null) itemBeanToUpdate.setLogrado(clienteProgramaItemDto.getLogrado());

        var updatedItemBean = _repository.save(itemBeanToUpdate);
        return _mapper.toDto(updatedItemBean);
    }

    @Override
    public boolean delete(Long programaId, Long clienteProgramaId, Long id) {
        var itemBean = _repository.findByIdCustom(programaId, clienteProgramaId, id);
        if(itemBean.isEmpty()) return false;
        itemBean.get().setActive(false);
        _repository.save(itemBean.get());
        return true;
    }
}
