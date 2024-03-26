package com.devs.powerfit.daos.arqueo;

import com.devs.powerfit.beans.arqueo.ArqueoBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArqueoDao extends JpaRepository<ArqueoBean,Long> {
}
