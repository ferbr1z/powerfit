package com.devs.powerfit.daos.suscripciones;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuscripcionDao extends JpaRepository<SuscripcionBean, Long> {
    Optional<SuscripcionBean> findByIdAndActiveTrue(Long id);
    Optional<SuscripcionBean> findByClienteIdAndActiveTrue(Long id);
    Page<SuscripcionBean> findAllByActiveTrue(Pageable pageable);


}
