package com.devs.powerfit.controllers.clientes;

import com.devs.powerfit.dtos.clientes.ClienteDto;
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
    @Autowired
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ClienteDto> create(@RequestBody ClienteDto clienteDto) {
        return new ResponseEntity<>(clienteService.create(clienteDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(clienteService.getById(id), HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<ClienteDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(clienteService.getAll(page), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDto> update(@PathVariable Long id, @RequestBody ClienteDto clienteDto) {
        return new ResponseEntity<>(clienteService.update(id, clienteDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return new ResponseEntity<>(clienteService.delete(id), HttpStatus.OK);
    }

}