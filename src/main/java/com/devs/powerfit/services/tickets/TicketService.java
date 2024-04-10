package com.devs.powerfit.services.tickets;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.tickets.TicketBean;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.daos.cajas.CajaDao;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.daos.tickets.TicketDao;
import com.devs.powerfit.dtos.tickets.TicketDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.tickets.ITicketService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.ticketMappers.TicketMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class TicketService implements ITicketService {
    private final SesionCajaDao sesionCajaDao;
    private final TicketMapper mapper;
    private final TicketDao ticketDao;
    private final CajaDao cajaDao;
    private final UsuarioDao usuarioDao;
    @Autowired
    public TicketService(SesionCajaDao sesionCajaDao, TicketMapper mapper, TicketDao ticketDao, CajaDao cajaDao, UsuarioDao usuarioDao) {
        this.sesionCajaDao = sesionCajaDao;
        this.mapper = mapper;
        this.ticketDao = ticketDao;
        this.cajaDao = cajaDao;
        this.usuarioDao = usuarioDao;
    }

    @Override
    public TicketDto create(TicketDto ticketDto) {
        if (ticketDto.getTimbrado()==null || ticketDto.getSesionId()==null|| ticketDto.getTotal()==null){
            throw new BadRequestException("Los campos timbrado, sesionID y total son obligatorios para crear un ticket");
        }
        var sesionOptional= sesionCajaDao.findByIdAndActiveTrue(ticketDto.getSesionId());
        if (sesionOptional.isEmpty()){
            throw new BadRequestException("No existe sesion con ese id");
        }
        SesionCajaBean sesion=sesionOptional.get();
        String numeroTicketCompleto=obtenerNumeroTicketCompleto(ticketDto.getSesionId());
        double ivaTotal=ticketDto.getIvaTotal()!= null ? ticketDto.getIvaTotal() : ticketDto.getIva5() + ticketDto.getIva10();
        if (ticketDto.getIvaTotal()!=null && ticketDto.getIvaTotal()!=ivaTotal){
            throw new BadRequestException("El valor de ivaTotal proporcionado no coincide con el cálculo");
        }
        double total= ticketDto.getTotal()!=null ? ticketDto.getTotal() : ticketDto.getSubTotal() + ivaTotal;
        if(ticketDto.getTotal()!=null && ticketDto.getTotal()!=total){
            throw new BadRequestException("El valor de total proporcionado no coincide con el cálculo");
        }
        LocalDate fecha = ticketDto.getFecha() != null ? ticketDto.getFecha() : LocalDate.now();
        TicketBean ticket= new TicketBean();
        ticket.setSesion(sesion);
        ticket.setTimbrado(ticketDto.getTimbrado());
        ticket.setNroTicket(numeroTicketCompleto);
        ticket.setFecha(fecha);
        ticket.setTotal(total);
        ticket.setSubTotal(ticketDto.getSubTotal() != null ? ticketDto.getSubTotal() : total - ivaTotal);
        ticket.setSaldo(ticketDto.getSaldo() != null ? ticketDto.getSaldo() : total);
        ticket.setIva5(ticketDto.getIva5() != null ? ticketDto.getIva5() : 0.0);
        ticket.setIva10(ticketDto.getIva10() != null ? ticketDto.getIva10() : 0.0);
        ticket.setIvaTotal(ivaTotal);
        ticket.setPagado(false);
        ticket.setActive(true);
        ticket.setNombreCaja(obtenerNombreDeCaja(sesion.getId()));
        ticket.setNombreEmpleado(obtenerNombreDeEmpleado(sesion.getId()));
        TicketBean savedTicket=ticketDao.save(ticket);
        return mapper.toDto(savedTicket);
    }
    @Override
    public TicketDto getById(Long id) {
        var ticket = ticketDao.findByIdAndActiveTrue(id);
        if (ticket.isPresent()) {
            return mapper.toDto(ticket.get());
        }
        throw new NotFoundException("ticket no encontrado");
    }

    @Override
    public PageResponse<TicketDto> getAll(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var ticketPage = ticketDao.findAllByActiveTrue(pageRequest);
        if (ticketPage.isEmpty()) {
            throw new NotFoundException("No hay tickets en la lista");
        }
        var ticketDtoPage = ticketPage.map(mapper::toDto);
        return new PageResponse<>(ticketDtoPage.getContent(),
                ticketDtoPage.getTotalPages(),
                ticketDtoPage.getTotalElements(),
                ticketDtoPage.getNumber() + 1);
    }

    @Override
    public TicketDto update(Long id, TicketDto ticketDto) {
        // Verificar si el ticket con el ID proporcionado existe
        TicketBean existingTicket = ticketDao.findById(id)
                .orElseThrow(() -> new NotFoundException("La ticket con ID " + id + " no existe"));
        // Verificar si los campos obligatorios no están incompletos
        if (ticketDto.getTimbrado() == null || ticketDto.getNroTicket() == null || ticketDto.getTotal() == null) {
            throw new BadRequestException("Los campos  timbrado, nroticket y total son obligatorios para actualizar una ticket");
        }

        // Verificar si la ticket ya existe con el nuevo número de ticket
        if (!existingTicket.getNroTicket().equals(ticketDto.getNroTicket()) && ticketDao.existsByNroTicket(ticketDto.getNroTicket())) {
            throw new BadRequestException("Ya existe una ticket con el número " + ticketDto.getNroTicket());
        }

        // Comprobar que los valores no sean negativos
        if (ticketDto.getSubTotal() < 0 || ticketDto.getIva5() < 0 || ticketDto.getIva10() < 0 || ticketDto.getTotal() < 0) {
            throw new BadRequestException("Los valores subTotal, iva5, iva10 y total no pueden ser negativos");
        }

        // Calcular el ivaTotal si no se proporciona explícitamente
        double ivaTotal = ticketDto.getIvaTotal() != null ? ticketDto.getIvaTotal() : ticketDto.getIva5() + ticketDto.getIva10();

        // Verificar si los datos de ivaTotal y total son correctos
        if (ticketDto.getIvaTotal() != null && ticketDto.getIvaTotal() != ivaTotal) {
            throw new BadRequestException("El valor de ivaTotal proporcionado no coincide con el cálculo");
        }

        double total =  ticketDto.getSubTotal() + ivaTotal;

        if (ticketDto.getTotal() != total) {
            throw new BadRequestException("El valor de total proporcionado no coincide con el cálculo");
        }

        // Convertir la fecha de String a Date
        LocalDate fecha = ticketDto.getFecha() != null ? ticketDto.getFecha() : LocalDate.now();

        existingTicket.setTimbrado(ticketDto.getTimbrado());
        existingTicket.setNroTicket(ticketDto.getNroTicket());
        existingTicket.setFecha(fecha);
        existingTicket.setTotal(total);
        existingTicket.setSubTotal(ticketDto.getSubTotal() != null ? ticketDto.getSubTotal() : total - ivaTotal);
        existingTicket.setSaldo(ticketDto.getSaldo() != null ? ticketDto.getSaldo() : 0.0);
        existingTicket.setIva5(ticketDto.getIva5() != null ? ticketDto.getIva5() : 0.0);
        existingTicket.setIva10(ticketDto.getIva10() != null ? ticketDto.getIva10() : 0.0);
        existingTicket.setIvaTotal(ivaTotal);
        existingTicket.setPagado(ticketDto.isPagado());

        // Guardar los cambios en la base de datos
        TicketBean updatedTicket = ticketDao.save(existingTicket);

        // Retornar la ticketDto actualizada
        return mapper.toDto(updatedTicket);
    }

    @Override
    public boolean delete(Long id) {
        var ticket = ticketDao.findByIdAndActiveTrue(id);
        if (ticket.isPresent()) {
            var ticketBean = ticket.get();
            ticketBean.setActive(false);
            ticketDao.save(ticketBean);
            return true;
        }
        throw new NotFoundException("ticket no encontrado");
    }
    @Override
    public PageResponse<TicketDto> searchByPendiente(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var ticketPage = ticketDao.findAllByPagado(pageRequest,false);
        if (ticketPage.isEmpty()) {
            throw new NotFoundException("No hay tickets en la lista");
        }
        var ticketDtoPage = ticketPage.map(mapper::toDto);
        return new PageResponse<>(ticketDtoPage.getContent(),
                ticketDtoPage.getTotalPages(),
                ticketDtoPage.getTotalElements(),
                ticketDtoPage.getNumber() + 1);
    }
    @Override
    public PageResponse<TicketDto> searchByPagado(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var ticketPage = ticketDao.findAllByPagado(pageRequest,true);
        if (ticketPage.isEmpty()) {
            throw new NotFoundException("No hay tickets en la lista");
        }
        var ticketDtoPage = ticketPage.map(mapper::toDto);
        return new PageResponse<>(ticketDtoPage.getContent(),
                ticketDtoPage.getTotalPages(),
                ticketDtoPage.getTotalElements(),
                ticketDtoPage.getNumber() + 1);
    }

    @Override
    public TicketDto searchByNumeroTicket(String numeroTicket) {
        var ticket = ticketDao.findByNroTicketIgnoreCaseAndActiveTrue(numeroTicket);
        if (ticket.isPresent()) {
            return mapper.toDto(ticket.get());
        }
        throw new NotFoundException("ticket no encontrado");
    }
    @Override
    public TicketDto modificarPagado(Long id, boolean pagado) {
        // Verificar si el ticket con el ID proporcionado existe
        TicketBean ticket = ticketDao.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("El ticket con ID " + id + " no existe"));
        // Actualizar el estado de pago del ticket
        ticket.setPagado(pagado);
        // Guardar los cambios en la base de datos
        TicketBean ticketActualizado = ticketDao.save(ticket);
        // Retornar el ticket actualizado
        return mapper.toDto(ticketActualizado);
    }
    public TicketDto actualizarSaldo(Long id, double nuevoSaldo) {
        TicketBean ticket = ticketDao.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("El ticket con ID " + id + " no existe"));
        ticket.setSaldo(nuevoSaldo);
        TicketBean ticketActualizado = ticketDao.save(ticket);
        return mapper.toDto(ticketActualizado);
    }
    private String obtenerNumeroTicketCompleto(Long sesionId) {
        Optional<SesionCajaBean> sesionOptional = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesionOptional.isPresent()) {
            SesionCajaBean sesionBean = sesionOptional.get();
            Optional<CajaBean> cajaOptional = cajaDao.findByIdAndActiveTrue(sesionBean.getCaja().getId());
            if (cajaOptional.isPresent()) {
                CajaBean cajaBean = cajaOptional.get();
                Long numeroTicket = cajaBean.getNumeroTicket();
                if (numeroTicket == null) {
                    numeroTicket = 1L;
                } else {
                    numeroTicket++;
                }
                cajaBean.setNumeroTicket(numeroTicket);

                String sucursal = "001";
                String numeroCajaFormatted = String.format("%03d", cajaBean.getNumeroCaja());
                String numeroTicketFormatted = String.format("%08d", numeroTicket);

                String numeroTicketCompleto = sucursal + "-" + numeroCajaFormatted + "-" + numeroTicketFormatted;
                cajaDao.save(cajaBean);

                return numeroTicketCompleto;
            } else {
                throw new BadRequestException("La caja asociada a la sesión no fue encontrada.");
            }
        } else {
            throw new BadRequestException("La sesión no fue encontrada.");
        }
    }
    private String obtenerNombreDeCaja(Long sesionId) {
        Optional<SesionCajaBean> sesionOptional = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesionOptional.isPresent()) {
            SesionCajaBean sesionBean = sesionOptional.get();
            Optional<CajaBean> cajaOptional = cajaDao.findByIdAndActiveTrue(sesionBean.getCaja().getId());
            if (cajaOptional.isPresent()) {
                CajaBean cajaBean = cajaOptional.get();
                return cajaBean.getNombre();
            } else {
                throw new BadRequestException("La caja asociada a la sesión no fue encontrada.");
            }
        } else {
            throw new BadRequestException("La sesión no fue encontrada.");
        }
    }
    private String obtenerNombreDeEmpleado(Long sesionId) {
        Optional<SesionCajaBean> sesionOptional = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesionOptional.isPresent()) {
            SesionCajaBean sesionBean = sesionOptional.get();
            Optional<UsuarioBean> cajaOptional = usuarioDao.findByIdAndActiveTrue(sesionBean.getUsuario().getId());
            if (cajaOptional.isPresent()) {
                UsuarioBean usuarioBean = cajaOptional.get();
                return usuarioBean.getNombre();
            } else {
                throw new BadRequestException("La caja asociada a la sesión no fue encontrada.");
            }
        } else {
            throw new BadRequestException("La sesión no fue encontrada.");
        }
    }
}
