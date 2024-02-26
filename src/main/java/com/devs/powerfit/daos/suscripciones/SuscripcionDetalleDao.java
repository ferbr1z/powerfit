package com.devs.powerfit.daos.suscripciones;

import com.devs.powerfit.beans.suscripciones.SuscripcionDetalleBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuscripcionDetalleDao extends JpaRepository<SuscripcionDetalleBean,Long>{
}
