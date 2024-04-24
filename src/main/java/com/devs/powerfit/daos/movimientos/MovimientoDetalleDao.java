package com.devs.powerfit.daos.movimientos;

import com.devs.powerfit.beans.movimientos.MovimientoBean;
import com.devs.powerfit.beans.movimientos.MovimientoDetalleBean;
import com.devs.powerfit.beans.tipoDePagos.TipoDePagoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoDetalleDao extends JpaRepository<MovimientoDetalleBean,Long> {
    Optional<MovimientoDetalleBean> findByIdAndActiveTrue(Long id);
    Page<MovimientoDetalleBean> findAllByActiveTrue(Pageable pageable);
    Page<MovimientoDetalleBean>findAllByMovimientoAndActiveTrue(Pageable pageable, MovimientoBean movimiento);
    List<MovimientoDetalleBean> findAllByMovimientoAndActiveTrue(MovimientoBean movimiento);
    Page<MovimientoDetalleBean>findAllByTipoDePagoAndActiveTrue(Pageable pageable, TipoDePagoBean tipoDePagoBean);
    List<MovimientoDetalleBean> findAllByTipoDePagoAndActiveTrue(TipoDePagoBean tipoDePagoBean);

}
