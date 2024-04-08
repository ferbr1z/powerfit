package com.devs.powerfit.daos.tickets;

import com.devs.powerfit.beans.tickets.TicketDetalleBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketDetalleDao extends JpaRepository<TicketDetalleBean,Long> {
    Optional<TicketDetalleBean> findByIdAndActiveTrue(Long id);
    Page<TicketDetalleBean> findAllByActiveTrue(Pageable pageable);
    List<TicketDetalleBean> findAllByTicketIdAndActiveTrue(Long id);

    List<TicketDetalleBean> findAllByActiveIsTrue();

    boolean existsByTicketId(Long TicketId);
}
