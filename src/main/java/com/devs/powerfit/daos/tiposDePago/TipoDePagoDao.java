package com.devs.powerfit.daos.tiposDePago;

import com.devs.powerfit.beans.tipoDePagos.TipoDePagoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoDePagoDao extends JpaRepository<TipoDePagoBean,Long> {
    Optional<TipoDePagoBean> findByIdAndActiveTrue(Long id);
    Page<TipoDePagoBean> findAllByActiveTrue(Pageable pageable);
    Page<TipoDePagoBean> findAllByNombreContainingIgnoreCaseAndActiveTrue(Pageable pageable, String nombre);
}
