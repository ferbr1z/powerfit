package com.devs.powerfit.daos.suscripciones;

import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.dtos.suscripciones.SuscripcionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuscripcionDao extends JpaRepository<SuscripcionBean, Long> {
}
