package com.devs.powerfit.daos.facturas;

import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.beans.facturas.FacturaProveedorBean;
import com.devs.powerfit.beans.facturas.FacturaProveedorDetalleBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface FacturaProveedorDao extends JpaRepository<FacturaProveedorBean,Long> {
    Page<FacturaProveedorBean> findAllByNombreProveedorContainingIgnoreCaseAndActiveIsTrue(Pageable pageable, String nombre);
    Page<FacturaProveedorBean>findAllByRucProveedorIgnoreCaseAndActiveTrue(Pageable pageable, String ruc);
    Optional<FacturaProveedorBean>findByNroFacturaIgnoreCaseAndActiveTrue(String nroFactura);
    Optional<FacturaProveedorBean> findByIdAndActiveTrue(Long id);
    Page<FacturaProveedorBean> findAllByActiveTrue(Pageable pageable);
    Page<FacturaProveedorBean> findAllByPagado(Pageable pageable, boolean pagado);
    boolean existsByNroFactura(String nroFactura);

    Page<FacturaProveedorBean> findAllByFechaBetween(Pageable pageRequest, LocalDate fechaInicio, LocalDate fechaFin);
}
