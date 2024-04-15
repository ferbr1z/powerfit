package com.devs.powerfit.daos.cajas;

import com.devs.powerfit.beans.cajas.ExtraccionDeCajaBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ExtraccionCajaDao extends JpaRepository<ExtraccionDeCajaBean, Long> {
    Optional<ExtraccionDeCajaBean> findByIdAndActiveTrue(Long id);

    Page<ExtraccionDeCajaBean> findAllByActiveIsTrue(Pageable pageable);

    Page<ExtraccionDeCajaBean> findAllByActiveIsTrueAndFechaBetween(Pageable pageable, LocalDate fehcaInicio, LocalDate fechaFin);
}
