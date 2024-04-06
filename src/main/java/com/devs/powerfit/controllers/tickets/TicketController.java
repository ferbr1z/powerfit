package com.devs.powerfit.controllers.tickets;

import com.devs.powerfit.dtos.tickets.TicketConDetalleDto;
import com.devs.powerfit.dtos.tickets.TicketDto;
import com.devs.powerfit.interfaces.tickets.ITicketConDetalleService;
import com.devs.powerfit.interfaces.tickets.ITicketService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final ITicketConDetalleService ticketConDetalleService;
    private final ITicketService ticketService;

    @Autowired
    public TicketController(ITicketConDetalleService ticketConDetalleService, ITicketService ticketService) {
        this.ticketConDetalleService = ticketConDetalleService;
        this.ticketService = ticketService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @PostMapping
    public ResponseEntity<TicketConDetalleDto> create(@RequestBody TicketConDetalleDto ticketDto) {
        return new ResponseEntity<>(ticketConDetalleService.create(ticketDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/{id}")
    public ResponseEntity<TicketConDetalleDto> getById(@PathVariable Long id) {
        return new ResponseEntity<>(ticketConDetalleService.getById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<TicketConDetalleDto>> getAll(@PathVariable int page) {
        return new ResponseEntity<>(ticketConDetalleService.getAll(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/numero/{numero}")
    public ResponseEntity<TicketConDetalleDto> getByNumeroTicket(@PathVariable String numero) {
        return new ResponseEntity<>(ticketConDetalleService.getByNumeroTicket(numero), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/estado/pagado/page/{page}")
    public ResponseEntity<PageResponse<TicketConDetalleDto>> searchByPagado(@PathVariable int page) {
        return new ResponseEntity<>(ticketConDetalleService.searchByPagado(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/estado/pendiente/page/{page}")
    public ResponseEntity<PageResponse<TicketConDetalleDto>> searchByPendiente(@PathVariable int page) {
        return new ResponseEntity<>(ticketConDetalleService.searchByPendiente(page), HttpStatus.OK);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/numero/{numero}")
    public ResponseEntity<TicketDto> searchCabeceraByNumeroTicket(@PathVariable String numero) {
        return new ResponseEntity<>(ticketService.searchByNumeroTicket(numero), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/estado/pagado/page/{page}")
    public ResponseEntity<PageResponse<TicketDto>> searchCabeceraByPagado(@PathVariable int page) {
        return new ResponseEntity<>(ticketService.searchByPagado(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/estado/pendiente/page/{page}")
    public ResponseEntity<PageResponse<TicketDto>> searchCabeceraByPendiente(@PathVariable int page) {
        return new ResponseEntity<>(ticketService.searchByPendiente(page), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','CAJERO')")
    @GetMapping("/cabecera/page/{page}")
    public ResponseEntity<PageResponse<TicketDto>> getAllCabecera(@PathVariable int page) {
        return new ResponseEntity<>(ticketService.getAll(page), HttpStatus.OK);
    }
}
