package com.devs.powerfit.daos.facturas;

import com.devs.powerfit.beans.facturas.FacturaDetalleBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaDetalleDao extends JpaRepository<FacturaDetalleBean,Long> {
    Optional<FacturaDetalleBean> findByIdAndActiveTrue(Long id);
    Page<FacturaDetalleBean> findAllByActiveTrue(Pageable pageable);
    List<FacturaDetalleBean> findAllByFacturaIdAndActiveTrue( Long id);

    boolean existsByFacturaIdAndSuscripcionId(Long facturaId, Long suscripcionId);
}
