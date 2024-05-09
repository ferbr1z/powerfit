package com.devs.powerfit.controllers.clientes;

import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.clientes.ClienteListaDto;
import com.devs.powerfit.dtos.clientes.NuevosClientesDto;
import com.devs.powerfit.dtos.clientes.PagoClienteDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionGananciasDto;
import com.devs.powerfit.dtos.suscripciones.SuscripcionesEstadisticasDto;
import com.devs.powerfit.services.clientes.ClienteListaService;
import com.devs.powerfit.services.clientes.ReportesClienteService;
import com.devs.powerfit.services.movimientos.MovimientoPorClienteService;
import com.devs.powerfit.utils.responses.PageResponse;
import com.devs.powerfit.interfaces.clientes.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final IClienteService clienteService;
    private final ClienteListaService clienteListaService;
    private final ReportesClienteService reportesClienteService;
    private final MovimientoPorClienteService movimientoPorClienteService;


    @Autowired
    public ClienteController(IClienteService clienteService, ClienteListaService clienteListaService, MovimientoPorClienteService movimientoPorClienteService,ReportesClienteService reportesClienteService) {
        this.clienteService = clienteService;
        this.clienteListaService = clienteListaService;
        this.movimientoPorClienteService = movimientoPorClienteService;
        this.reportesClienteService = reportesClienteService;
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

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/nuevos/{fechaInicio}/{fechaFin}/page/{page}")
    public ResponseEntity<PageResponse<ClienteDto>> getByFechaRegistro(@PathVariable int page, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        return new ResponseEntity<>(clienteListaService.obtenerClientesNuevosConDetalles(page, fechaInicio, fechaFin), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO','CLIENTE')")
    @GetMapping("/id/{id}/page/{page}")
    public ResponseEntity<PageResponse<PagoClienteDto>> getAllPagosCliente(@PathVariable Long id , @PathVariable int page) {
        return new ResponseEntity<>(movimientoPorClienteService.obtenerPagosPorCliente(id,page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/reportes/nuevos/{fechaInicio}/{fechaFin}")
    ResponseEntity<NuevosClientesDto> countBetween(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin){
        return new ResponseEntity<>(clienteListaService.obtenerClientesNuevos(fechaInicio, fechaFin), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/reportes/cantidad-por-estado-suscripcion")
    public ResponseEntity<SuscripcionesEstadisticasDto> getCantidadClientesPorEstado() {
        return new ResponseEntity<>(reportesClienteService.cantidadClientesPorEstadoSuscripcion(), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/reportes/ganancias")
    public ResponseEntity<SuscripcionGananciasDto> getGanancias() {
        return new ResponseEntity<>(reportesClienteService.calcularGanancias(), HttpStatus.OK);
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
    @PreAuthorize("hasAnyAuthority('ADMIN','ENTRENADOR','CAJERO')")
    @GetMapping("/searchByRuc/{ruc}/page/{page}")
    public ResponseEntity<PageResponse<ClienteDto>> searchByRuc(@PathVariable int page,@PathVariable String ruc) {
        return new ResponseEntity<>(clienteService.searchByRuc(ruc,page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/createClientAccounts")
    public String createAccounts() {
        return clienteService.createAccountsForClientsWithoutUsuario();
    }
}
