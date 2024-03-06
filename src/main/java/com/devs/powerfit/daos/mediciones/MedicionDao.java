package com.devs.powerfit.daos.mediciones;

import com.devs.powerfit.beans.mediciones.MedicionBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Date;
@Repository
public interface MedicionDao extends JpaRepository<MedicionBean, Long> {
    Optional<MedicionBean> findByClienteIdAndActiveTrue(Long id);
    //Optional<MedicionBean> findByFechaAndActiveTrue(Date fecha);
    //Optional<MedicionBean> findByFechaBetween(Date fechaInicio, Date fechaFin);
    //Optional<MedicionBean> findByClienteIdAndFechaBetween(Long id, Date fechaInicio, Date fechaFin);
    //Long countByClienteId(Long id);
    //MedicionBean findTopByClienteIdOrderByFechaDesc(Long id);
    //Optional<MedicionBean> findByImcGreaterThan(Double valorIMC);
    Page<MedicionBean> findAllByActiveTrue(Pageable pageable);
    Page<MedicionBean> findAllByClienteIdAndActiveTrue(Pageable pageable, Long id);
    //Page<MedicionBean> findAllByFechaAndActiveTrue(Pageable pageable);
    //Page<MedicionBean> findAllByFechaBetween(Pageable pageable);
    //Page<MedicionBean> findAllByClienteIdAndFechaBetween(Pageable pageable);
}
