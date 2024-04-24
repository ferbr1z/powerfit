package com.devs.powerfit.daos.facturas;

import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.facturas.FacturaBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface FacturaDao extends JpaRepository<FacturaBean,Long> {
    Page<FacturaBean> findAllByNombreClienteContainingIgnoreCaseAndActiveIsTrue(Pageable pageable,String nombre);
    Page<FacturaBean>findAllByRucClienteIgnoreCaseAndActiveTrue(Pageable pageable, String ruc);
    Optional<FacturaBean>findByNroFacturaIgnoreCaseAndActiveTrue(String nroFactura);
    Optional<FacturaBean> findByIdAndActiveTrue(Long id);
    Page<FacturaBean> findAllByActiveTrue(Pageable pageable);
    boolean existsByNroFactura(String nroFactura);
    Page<FacturaBean>findAllByPagado(Pageable pageable,boolean pagado);
    Page<FacturaBean>findAllByNombreClienteContainingIgnoreCaseAndPagado(Pageable pageable,String nombre,boolean pagado);
    List<FacturaBean> findAllByFechaBetweenAndActiveTrue(LocalDate fechaInicio, LocalDate fechaFin);
    Page<FacturaBean> findAllByClienteAndPagadoTrueAndActiveTrue(Pageable pageable, ClienteBean clienteBean);
    Page<FacturaBean> findAllByFechaBetween(Pageable pageRequest, LocalDate fechaInicio, LocalDate fechaFin);
}
