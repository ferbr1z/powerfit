package com.devs.powerfit.services.programas;

import com.devs.powerfit.daos.programas.ClienteProgramaDao;
import com.devs.powerfit.daos.programas.ProgramaDao;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import com.devs.powerfit.interfaces.programas.IProgramaClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.programaMapper.ClienteProgramaMapper;
import com.devs.powerfit.utils.mappers.programaMapper.ProgramaMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ProgramaClienteService implements IProgramaClienteService {
    private ProgramaMapper _mapper;

    private ClienteProgramaMapper _clienteProgramaMapper;
    private ProgramaDao _repository;
    private ClienteProgramaDao _clienteProgramaRepository;
    private IClienteService _clienteService;


    @Autowired
    public ProgramaClienteService(ProgramaDao repository, ClienteProgramaDao clienteProgramaRepository, ProgramaMapper mapper, ClienteProgramaMapper clienteProgramaMapper, IClienteService clienteService) {
        _repository = repository;
        _clienteProgramaRepository = clienteProgramaRepository;
        _mapper = mapper;
        _clienteProgramaMapper = clienteProgramaMapper;
        _clienteService = clienteService;
    }

    @Override
    public ClienteProgramaDto registrarCliente(Long programaId, ClienteProgramaDto clienteProgramaDto) {

        var clienteId = clienteProgramaDto.getClienteId();
        if(_clienteService.getById(clienteId)==null) throw new NotFoundException("Cliente no encontrado");
        if(_repository.findByIdAndActiveTrue(programaId).isEmpty()) throw new NotFoundException("Programa no encontrado");

        clienteProgramaDto.setProgramaId(programaId);
        var clienteProgramaBean = _clienteProgramaMapper.toBean(clienteProgramaDto);
        clienteProgramaBean.setActive(true);
        _clienteProgramaRepository.save(clienteProgramaBean);
        var newClienteProgramaBean = _clienteProgramaRepository.findById(clienteProgramaBean.getId()).get();
        return _clienteProgramaMapper.toDto(newClienteProgramaBean);
    }

    @Override
    public ClienteProgramaDto getClienteProgramaById(Long programaId, Long id) {
        var clienteProgramaBean = _clienteProgramaRepository.findByProgramaIdAndId(programaId, id);
        if(clienteProgramaBean.isEmpty()) return null;
        return _clienteProgramaMapper.toDto(clienteProgramaBean.get());
    }

    @Override
    public PageResponse<ClienteProgramaDto> getClientesByProgramaId(Long programaId, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clienteProgramas = _clienteProgramaRepository.findAllByProgramaId(programaId, pag);
        var pageResponse = new PageResponse<ClienteProgramaDto>(
                clienteProgramas.getContent(),
                clienteProgramas.getTotalPages(),
                clienteProgramas.getTotalElements(),
                clienteProgramas.getNumber() + 1
        );

        return pageResponse;
    }

    @Override
    public ClienteProgramaDto updateClientePrograma(Long programaId, Long id, ClienteProgramaDto clienteProgramaDto) {


        // Obtenemos el registro del cliente
        var clienteProgramaBean = _clienteProgramaRepository.findByProgramaIdAndId(programaId, id);
        if(clienteProgramaBean.isEmpty()) return null; // si no existe, retornamos null, el controlador se encarga

        // Si el clienteProgramaDto trae un programaId, lo actualizamos si es que existe
        if(clienteProgramaDto.getProgramaId()!=null) {
            var programa = _repository.findByIdAndActiveTrue(clienteProgramaDto.getProgramaId());
            if(programa.isEmpty()) throw new NotFoundException("Programa no encontrado");
            clienteProgramaBean.get().getPrograma().setId(clienteProgramaDto.getProgramaId());
        }

        // Si el clienteProgramaDto trae un clienteId, lo actualizamos si es que existe
        if(clienteProgramaDto.getClienteId()!=null) {
            var clienteId = clienteProgramaDto.getClienteId();
            if(_clienteService.getById(clienteId)==null) throw new NotFoundException("Cliente no encontrado");
            clienteProgramaBean.get().getCliente().setId(clienteProgramaDto.getClienteId());
        }
        if(clienteProgramaDto.getFechaEvaluacion()!=null) clienteProgramaBean.get().setFechaEvaluacion(clienteProgramaDto.getFechaEvaluacion());


        var updated = _clienteProgramaRepository.save(clienteProgramaBean.get());

        return _clienteProgramaMapper.toDto(updated);
    }

    @Override
    public boolean deleteClientePrograma(Long programaId, Long id) {
        var clienteProgramaBean = _clienteProgramaRepository.findByProgramaIdAndId(programaId, id);
        if(clienteProgramaBean.isEmpty()) return false;
        clienteProgramaBean.get().setActive(false);
        _clienteProgramaRepository.save(clienteProgramaBean.get());
        return true;
    }

}
