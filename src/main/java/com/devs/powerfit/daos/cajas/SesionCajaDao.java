package com.devs.powerfit.daos.cajas;

import com.devs.powerfit.beans.cajas.SesionCajaBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SesionCajaDao extends JpaRepository<SesionCajaBean,Long> {
    Optional<SesionCajaBean> findByIdAndActiveTrue(Long id);
    Page<SesionCajaBean> findAllByActiveTrue(Pageable pageable);
    Page<SesionCajaBean> findAllByFechaBetweenAndActiveTrue(Pageable pageable, LocalDate fechaInicio,LocalDate fechaFin);
}
