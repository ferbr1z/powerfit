package com.devs.powerfit.services.programas;

import com.devs.powerfit.daos.programas.ClienteProgramaDao;
import com.devs.powerfit.daos.programas.ProgramaDao;
import com.devs.powerfit.daos.programas.ProgramaItemDao;
import com.devs.powerfit.dtos.programas.clientePrograma.BaseClienteProgramDto;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaItemDto;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import com.devs.powerfit.interfaces.programas.IProgramaClienteItemService;
import com.devs.powerfit.interfaces.programas.IProgramaClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.programaMapper.ClienteProgramaItemMapper;
import com.devs.powerfit.utils.mappers.programaMapper.ClienteProgramaMapper;
import com.devs.powerfit.utils.mappers.programaMapper.ProgramaItemMapper;
import com.devs.powerfit.utils.mappers.programaMapper.ProgramaMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProgramaClienteService implements IProgramaClienteService {
    private ProgramaMapper _mapper;

    private ClienteProgramaMapper _clienteProgramaMapper;
    private ProgramaDao _repository;
    private ClienteProgramaDao _clienteProgramaRepository;
    private IClienteService _clienteService;
    private IProgramaClienteItemService _programaClienteItemService;
    private ProgramaItemDao _programaItemRepository;

    @Autowired
    public ProgramaClienteService(ProgramaDao repository, ClienteProgramaDao clienteProgramaRepository,
                                  ProgramaMapper mapper, ClienteProgramaMapper clienteProgramaMapper,
                                  IClienteService clienteService, IProgramaClienteItemService programaClienteItemService,
                                  ProgramaItemDao programaItemRepository) {
        _repository = repository;
        _clienteProgramaRepository = clienteProgramaRepository;
        _mapper = mapper;
        _clienteProgramaMapper = clienteProgramaMapper;
        _clienteService = clienteService;
        _programaClienteItemService = programaClienteItemService;
        _programaItemRepository = programaItemRepository;
    }

    @Override
    @Transactional
    public BaseClienteProgramDto registrarCliente(Long programaId, BaseClienteProgramDto clienteProgramaDto) {

        var clienteId = clienteProgramaDto.getClienteId();
        if(_clienteService.getById(clienteId)==null) throw new NotFoundException("Cliente no encontrado");
        if(_repository.findByIdAndActiveTrue(programaId).isEmpty()) throw new NotFoundException("Programa no encontrado");

        clienteProgramaDto.setProgramaId(programaId);
        var clienteProgramaBean = _clienteProgramaMapper.toBean(clienteProgramaDto);

        clienteProgramaBean.setActive(true);
        //  poner el porcentaje en 0
        clienteProgramaBean.setPorcentaje(Double.parseDouble("0"));

        _clienteProgramaRepository.save(clienteProgramaBean);

        this.createClienteProgramaItems(programaId, clienteProgramaBean.getId());

        var newClienteProgramaBean = _clienteProgramaRepository.findById(clienteProgramaBean.getId()).get();
        var newClienteDto = _clienteProgramaMapper.toBaseDto(newClienteProgramaBean);

        return newClienteDto;
    }

    @Override
    public ClienteProgramaDto getClienteProgramaById(Long programaId, Long id) {
        var clienteProgramaBean = _clienteProgramaRepository.findByProgramaIdAndId(programaId, id);
        if(clienteProgramaBean.isEmpty()) return null;
        return _clienteProgramaMapper.toDto(clienteProgramaBean.get());
    }

    @Override
    public PageResponse<ClienteProgramaDto> getClientesByProgramaId(Long programaId, String nombreCliente, LocalDate fechaInicio, LocalDate fechaFin, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clienteProgramas = _clienteProgramaRepository.findAllByProgramaId(programaId, nombreCliente, fechaInicio, fechaFin, pag);
        var pageResponse = new PageResponse<ClienteProgramaDto>(
                clienteProgramas.getContent(),
                clienteProgramas.getTotalPages(),
                clienteProgramas.getTotalElements(),
                clienteProgramas.getNumber() + 1
        );

        return pageResponse;
    }

    @Override
    public PageResponse<ClienteProgramaDto> getAllByClienteEmail(String clienteEmail, LocalDate fechaInicio, LocalDate fechaFin, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var clienteProgramas = _clienteProgramaRepository.findAllByClienteEmail(clienteEmail, fechaInicio, fechaFin, pag);
        var pageResponse = new PageResponse<ClienteProgramaDto>(
                clienteProgramas.getContent(),
                clienteProgramas.getTotalPages(),
                clienteProgramas.getTotalElements(),
                clienteProgramas.getNumber() + 1
        );

        return pageResponse;
    }

    @Override
    @Transactional
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

        // Si el clienteProgramaDto trae items, los actualizamos si es que existen
        if(clienteProgramaDto.getClienteProgramaItem()!=null) {
            var clienteProgramaItemDtos = clienteProgramaDto.getClienteProgramaItem();
            this.updateClienteProgramaItems(programaId, id, clienteProgramaItemDtos);
            clienteProgramaBean.get().setPorcentaje(calcPorcentaje(clienteProgramaItemDtos));
        }

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

    private Double calcPorcentaje(List<ClienteProgramaItemDto> clienteProgramaItemDtos) {
        var total = (double) clienteProgramaItemDtos.size();
        var logrados = (double) clienteProgramaItemDtos.stream().filter(ClienteProgramaItemDto::getLogrado).count();
        return (logrados / total) * 100;
    }

    @Transactional
    public void createClienteProgramaItems(Long programaId, Long clienteProgramaId) {
        var programaItemMapper = new ProgramaItemMapper();
        var programaItems = _programaItemRepository.getAllByProgramaIdAAndActiveTrue(programaId);
        programaItems.forEach(item -> {
            var clienteProgramaItemDto = new ClienteProgramaItemDto();
            clienteProgramaItemDto.setClienteProgramaId(clienteProgramaId);
            clienteProgramaItemDto.setProgramaItem(programaItemMapper.toDto(item));
            _programaClienteItemService.create(programaId, clienteProgramaId, clienteProgramaItemDto);
        });
    }

    @Transactional
    public void updateClienteProgramaItems(Long programaId, Long clienteProgramaId, List<ClienteProgramaItemDto> clienteProgramaItemDtos) {
        clienteProgramaItemDtos.forEach(item -> {
            item.setClienteProgramaId(clienteProgramaId);
            _programaClienteItemService.update(programaId, clienteProgramaId, item.getId(), item);
        });
    }


}
