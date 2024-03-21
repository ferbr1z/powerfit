package com.devs.powerfit.controllers.clientes;

import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.clientes.ClienteListaDto;
import com.devs.powerfit.services.clientes.ClienteListaService;
import com.devs.powerfit.utils.responses.PageResponse;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final IClienteService clienteService;
    private final ClienteListaService clienteListaService;
    @Autowired
    public ClienteController(IClienteService clienteService, ClienteListaService clienteListaService) {
        this.clienteService = clienteService;
        this.clienteListaService = clienteListaService;
    }
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    public ResponseEntity<ClienteDto> create(@RequestBody ClienteDto clienteDto) {
        return new ResponseEntity<>(clienteService.create(clienteDto), HttpStatus.CREATED);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(clienteService.getById(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<ClienteDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(clienteService.getAll(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/lista/page/{page}")
    public ResponseEntity<PageResponse<ClienteListaDto>> getAllWithEstado(@PathVariable int page) {
        return new ResponseEntity<>(clienteListaService.obtenerTodosClientesConEstadoGeneral(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> update(@PathVariable Long id, @RequestBody ClienteDto clienteDto) {
        return new ResponseEntity<>(clienteService.update(id, clienteDto), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return new ResponseEntity<>(clienteService.delete(id), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/searchByName/{nombre}/page/{page}")
    public ResponseEntity<PageResponse<ClienteDto>> searchByName(@PathVariable int page,@PathVariable String nombre) {
        return new ResponseEntity<>(clienteService.searchByNombre(nombre,page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/searchByCi/{ci}/page/{page}")
    public ResponseEntity<PageResponse<ClienteDto>> searchByCi(@PathVariable int page,@PathVariable String ci) {
        return new ResponseEntity<>(clienteService.searchByCi(ci,page), HttpStatus.OK);
    }

}
