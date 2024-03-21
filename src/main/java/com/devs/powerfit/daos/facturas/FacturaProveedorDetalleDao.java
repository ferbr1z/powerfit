package com.devs.powerfit.daos.facturas;

import com.devs.powerfit.beans.facturas.FacturaDetalleBean;
import com.devs.powerfit.beans.facturas.FacturaProveedorDetalleBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaProveedorDetalleDao extends JpaRepository<FacturaProveedorDetalleBean,Long> {
    Optional<FacturaProveedorDetalleBean> findByIdAndActiveTrue(Long id);
    Page<FacturaProveedorDetalleBean> findAllByActiveTrue(Pageable pageable);
    List<FacturaProveedorDetalleBean> findAllByFacturaIdAndActiveTrue( Long id);

}
