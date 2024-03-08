package com.devs.powerfit.daos.cajas;

import com.devs.powerfit.beans.cajas.CajaBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CajaDao extends JpaRepository<CajaBean,Long> {
}
