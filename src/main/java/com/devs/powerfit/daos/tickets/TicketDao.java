package com.devs.powerfit.daos.tickets;

import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.beans.tickets.TicketBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketDao extends JpaRepository<TicketBean,Long> {

    Optional<TicketBean> findByNroTicketIgnoreCaseAndActiveTrue(String nroTicket);
    Optional<TicketBean> findByIdAndActiveTrue(Long id);
    Page<TicketBean> findAllByActiveTrue(Pageable pageable);
    boolean existsByNroTicket(String nroTicket);
    Page<TicketBean>findAllByPagado(Pageable pageable,boolean pagado);
}
