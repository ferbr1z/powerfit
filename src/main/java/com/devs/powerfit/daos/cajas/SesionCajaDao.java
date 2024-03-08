package com.devs.powerfit.daos.cajas;

import com.devs.powerfit.beans.cajas.SesionCajaBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SesionCajaDao extends JpaRepository<SesionCajaBean,Long> {
}
