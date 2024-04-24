package com.devs.powerfit.daos.arqueo;

import com.devs.powerfit.beans.arqueo.ArqueoDetalleBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArqueoDetalleDao extends JpaRepository<ArqueoDetalleBean, Long> {

    List<ArqueoDetalleBean> findByArqueo_IdAndActiveIsTrue(Long id);
}
