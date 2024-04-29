package com.devs.powerfit.controllers.programas;

import com.devs.powerfit.dtos.programas.*;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.programas.IProgramItemService;
import com.devs.powerfit.interfaces.programas.IProgramaClienteService;
import com.devs.powerfit.interfaces.programas.IProgramaService;
import com.devs.powerfit.utils.responses.PageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/programas")
public class ProgramaController {
    private IProgramaService _service;
    private IProgramaClienteService _clienteProgramaService;
    private IProgramItemService _programItemService;
    @Autowired
    public ProgramaController(IProgramaService service, IProgramaClienteService clienteProgramaService, IProgramItemService programItemService){
        _service = service;
        _clienteProgramaService = clienteProgramaService;
        _programItemService = programItemService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @PostMapping()
    public  ResponseEntity<ProgramaDto>  create(@RequestBody CrearAndUpdateProgramaDto programaDto){
        var newProgramaDto  = _service.create(programaDto);
        return new ResponseEntity<>(newProgramaDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}")
    public ResponseEntity<FullProgramaDto> getById(@PathVariable Long id){
        var programa = (FullProgramaDto) _service.getById(id);
        if(programa==null){
            throw new NotFoundException("Programa no encontrado");
        }
        return new ResponseEntity<>(programa, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<ProgramaForListDto>> getAll(
            @PathVariable int page,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String nivel,
            @RequestParam(required = false) String sexo
    ) {
        ENivelPrograma eNivel = null;
        ESexo eSexo = null;

        try{
            if(nivel !=null)
                eNivel = ENivelPrograma.valueOf(nivel.toUpperCase());
            if(sexo!=null)
                eSexo = ESexo.valueOf(sexo.toUpperCase());
        } catch (Exception e){
        }

        var programas = _service.getAll(page, titulo, eNivel, eSexo);
        return new ResponseEntity<>(programas, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ProgramaDto> update(@PathVariable Long id, @RequestBody CrearAndUpdateProgramaDto updateDto) {
        var updated = _service.update(id, updateDto);
        if(updated==null){
            throw new NotFoundException("Programa no encontrado");
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id){
        return new ResponseEntity<>(_service.delete(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @PostMapping("/{id}/items")
    public ResponseEntity<ProgramaItemDto> addItem(@PathVariable Long id, @RequestBody ProgramaItemDto itemDto){
        var item = _programItemService.createItem(id, itemDto);
        if(item==null){
            throw new NotFoundException("Programa no encontrado");
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}/items/{itemId}")
    public ResponseEntity<ProgramaItemDto> getItemById(@PathVariable Long id, @PathVariable Long itemId){
        var item = _programItemService.getItemById(id, itemId);
        if(item==null){
            throw new NotFoundException("Item no encontrado");
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }


    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}/items/page/{page}")
    public ResponseEntity<PageResponse<ProgramaItemDto>> getItemsByProgramaId(@PathVariable int page, @PathVariable Long id){
        var items = _programItemService.getItemsByProgramaId(id, page);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<ProgramaItemDto> updateItem(@PathVariable Long id, @PathVariable Long itemId, @RequestBody ProgramaItemDto itemDto){
        var item = _programItemService.updateItem(id, itemId, itemDto);
        if(item==null){
            throw new NotFoundException("Item no encontrado");
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Boolean> deleteItem(@PathVariable Long id, @PathVariable Long itemId){
        return new ResponseEntity<>(_programItemService.deleteItem(id, itemId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @PostMapping("/{id}/clientes")
    public ResponseEntity<ClienteProgramaDto> registrarCliente(@PathVariable Long id, @RequestBody @Valid ClienteProgramaDto clienteProgramaDto){
        var clientePrograma = _clienteProgramaService.registrarCliente(id,  clienteProgramaDto);
        return new ResponseEntity<>(clientePrograma, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}/clientes/{clienteProgramaId}")
    public ResponseEntity<ClienteProgramaDto> getRegistroClienteById(@PathVariable Long id, @PathVariable Long clienteProgramaId){
        var clientePrograma = _clienteProgramaService.getClienteProgramaById(id, clienteProgramaId);
        if(clientePrograma==null){
            throw new NotFoundException("Registro de cliente no encontrado");
        }
        return new ResponseEntity<>(clientePrograma, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @GetMapping("/{id}/clientes/page/{page}")
    public ResponseEntity<PageResponse<ClienteProgramaDto>> getResgistroClientesByProgramaId(@PathVariable int page, @PathVariable Long id){
        var clientes = _clienteProgramaService.getClientesByProgramaId(id, page);
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @PutMapping("/{id}/clientes/{clienteProgramaId}")
    public ResponseEntity<ClienteProgramaDto> updateRegistroCliente(@PathVariable Long id, @PathVariable Long clienteProgramaId, @RequestBody ClienteProgramaDto clienteProgramaDto){
        var clientePrograma = _clienteProgramaService.updateClientePrograma(id, clienteProgramaId, clienteProgramaDto);
        if(clientePrograma==null){
            throw new NotFoundException("Registro de cliente no encontrado");
        }
        return new ResponseEntity<>(clientePrograma, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR')")
    @DeleteMapping("/{id}/clientes/{clienteProgramaId}")
    public ResponseEntity<Boolean> deleteRegistroCliente(@PathVariable Long id, @PathVariable Long clienteProgramaId){
        return new ResponseEntity<>(_clienteProgramaService.deleteClientePrograma(id, clienteProgramaId), HttpStatus.OK);
    }

}
