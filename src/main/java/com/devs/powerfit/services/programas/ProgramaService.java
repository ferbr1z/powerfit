package com.devs.powerfit.services.programas;

import com.devs.powerfit.beans.programas.ProgramaBean;
import com.devs.powerfit.daos.programas.ClienteProgramaDao;
import com.devs.powerfit.daos.programas.ProgramaDao;
import com.devs.powerfit.daos.programas.ProgramaItemDao;
import com.devs.powerfit.dtos.programas.CrearAndUpdateProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaForListDto;
import com.devs.powerfit.dtos.programas.ProgramaItemDto;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import com.devs.powerfit.interfaces.actividades.IActividadService;
import com.devs.powerfit.interfaces.empleados.IEmpleadoService;
import com.devs.powerfit.interfaces.programas.IProgramaService;
import com.devs.powerfit.services.empleados.EmpleadoService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.programaMapper.ClienteProgramaMapper;
import com.devs.powerfit.utils.mappers.programaMapper.ProgramaItemMapper;
import com.devs.powerfit.utils.mappers.programaMapper.ProgramaMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


@Service
public class ProgramaService implements IProgramaService {

    private ProgramaMapper _mapper;
    private ProgramaItemMapper _itemMapper;
    private ClienteProgramaMapper _clienteProgramaMapper;
    private IEmpleadoService _empleadoService;
    private IActividadService _actividadService;
    private ProgramaDao _repository;
    private ProgramaItemDao _itemRepository;
    private ClienteProgramaDao _clienteProgramaRepository;
    private IClienteService _clienteService;

    @Autowired
    public ProgramaService(ProgramaMapper mapper,
                           ProgramaItemMapper itemMapper,
                           ProgramaDao repository,
                           ProgramaItemDao itemRepository,
                           ClienteProgramaMapper clienteProgramaMapper,
                           ClienteProgramaDao clienteProgramaRepository,
                           IClienteService clienteService,
                           IEmpleadoService empleadoService,
                           IActividadService actividadService){
        _mapper = mapper;
        _itemMapper = itemMapper;
        _repository = repository;
        _itemRepository = itemRepository;
        _clienteProgramaMapper = clienteProgramaMapper;
        _clienteProgramaRepository = clienteProgramaRepository;
        _clienteService = clienteService;
        _empleadoService = empleadoService;
        _actividadService = actividadService;
    }

    @Override
    public ProgramaDto create(ProgramaDto programaDto) {
        var crearProgramaDto = (CrearAndUpdateProgramaDto) programaDto;
        var entrenadorId = crearProgramaDto.getEmpleado();
        var actividadId = crearProgramaDto.getActividad();

        if(_empleadoService.getById(entrenadorId)==null) throw new NotFoundException("Entrenador no encontrado");
        if(_actividadService.getById(actividadId)==null) throw new NotFoundException("Actividad no encontrada");

        ProgramaBean newPrograma = _mapper.toBean(crearProgramaDto);
        newPrograma.setActive(true);
        _repository.save(newPrograma);
        return _mapper.toCreateAndUpdateDto(newPrograma);
    }

    @Override
    public ProgramaDto getById(Long id) {
        var programa = _repository.findByIdAndActiveTrue(id);
        if(programa.isEmpty()) return null;
        var programaDto = _mapper.toFullDto(programa.get());
        return programaDto;
    }

    @Override
    public PageResponse<ProgramaDto> getAll(int page) {
        return null;
    }

    @Override
    public PageResponse<ProgramaForListDto> getAll(int page, String titulo, ENivelPrograma nivel, ESexo sexo) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        Page<ProgramaForListDto> programas = _repository.findAll(titulo, nivel, sexo, pag);
        var pageResponse = new PageResponse<ProgramaForListDto>(
                programas.getContent(),
                programas.getTotalPages(),
                programas.getTotalElements(),
                programas.getNumber() + 1);

        return pageResponse;
    }

    @Override
    public ProgramaDto update(Long id, ProgramaDto programaDto) {
        var programa = _repository.findByIdAndActiveTrue(id);
        if(programa.isEmpty()) return null;
        var updated = (CrearAndUpdateProgramaDto) programaDto;
        var updatedBean = _mapper.toBean(updated);

        if(programa.isEmpty()) return null;

        if(updated.getEmpleado() != null){
            programa.get().setEmpleado(updatedBean.getEmpleado());
        }

        if(updated.getActividad()!=null){
            programa.get().setActividad(updatedBean.getActividad());
        }

        if(updatedBean.getTitulo()!=null){
            programa.get().setTitulo(updatedBean.getTitulo());
        }

        if(updatedBean.getNivel()!=null){
            programa.get().setNivel(updatedBean.getNivel());
        }

        if(updatedBean.getSexo()!=null){
            programa.get().setSexo(updatedBean.getSexo());
        }

        _repository.save(programa.get());
        return _mapper.toCreateAndUpdateDto(programa.get());
    }

    @Override
    public boolean delete(Long id) {
        var programa = _repository.findByIdAndActiveTrue(id);

        if(programa.isEmpty()) return false;

        var items = _itemRepository.findAllByProgramaId(id);

        for (var item: items) {
            item.setActive(false);
            _itemRepository.save(item);
        }

        programa.get().setActive(false);
        _repository.save(programa.get());
        return true;
    }

    /********************************
     *
     *  ACA VA TODO LO DE LOS ITEMS
     *
     ********************************/

    @Override
    public ProgramaItemDto createItem(Long programaId, ProgramaItemDto itemDto) {
        var programa = _repository.findByIdAndActiveTrue(programaId);
        if(programa.isEmpty()) throw new NotFoundException("Programa no encontrado");
        var item = _itemMapper.toBean(itemDto);
        item.setPrograma(programa.get());
        item.setActive(true);
        _itemRepository.save(item);
        return _itemMapper.toDto(item);
    }

    @Override
    public ProgramaItemDto getItemById(Long programaId, Long itemId) {
        var item = _itemRepository.findByIdAAndProgramaIdAndActiveTrue(itemId, programaId);
        if(item.isEmpty()) throw new NotFoundException("Item no encontrado");
        return _itemMapper.toDto(item.get());
    }

    @Override
    public PageResponse<ProgramaItemDto> getItemsByProgramaId(Long programaId, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var items = _itemRepository.findAllByProgramaId(pag, programaId);

        var itemsDto = items.map(item -> _itemMapper.toDto(item));

        var pageResponse = new PageResponse<ProgramaItemDto>(
                itemsDto.getContent(),
                items.getTotalPages(),
                items.getTotalElements(),
                items.getNumber() + 1);
        return pageResponse;
    }

    @Override
    public ProgramaItemDto updateItem(Long programaId, Long itemId, ProgramaItemDto itemDto) {
        var item = _itemRepository.findByIdAndActiveTrue(itemId);
        if(item.isEmpty()) throw new NotFoundException("Item no encontrado");

        var programa = _repository.findByIdAndActiveTrue(programaId);
        if(programa.isEmpty()) throw new NotFoundException("Programa no encontrado");

        if(itemDto.getNombre()!=null) item.get().setNombre(itemDto.getNombre());
        if(itemDto.getDescripcion()!=null) item.get().setDescripcion(itemDto.getDescripcion());
        if(itemDto.getTiempo()!=null) item.get().setTiempo(itemDto.getTiempo());
        if(itemDto.getRepeticiones()!=null) item.get().setRepeticiones(itemDto.getRepeticiones());

        _itemRepository.save(item.get());
        return _itemMapper.toDto(item.get());
    }

    @Override
    public boolean deleteItem(Long programaId, Long itemId) {
        var item = _itemRepository.findByIdAndActiveTrue(itemId);
        if(item.isEmpty()) throw new NotFoundException("Item no encontrado");
        item.get().setActive(false);
        _itemRepository.save(item.get());
        return true;
    }

    /********************************
     *
     *  ACA VA TODO LO DE LOS CLIENTES
     *
     ********************************/

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
