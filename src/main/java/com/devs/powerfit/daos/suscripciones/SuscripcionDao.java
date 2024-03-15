package com.devs.powerfit.daos.suscripciones;

import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.enums.EEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionDao extends JpaRepository<SuscripcionBean,Long>{
    Optional<SuscripcionBean> findByIdAndActiveTrue(Long id);
    Page<SuscripcionBean> findAllByActiveTrue(Pageable pageable);
    Page<SuscripcionBean> findAllByClienteIdAndActiveTrue(Pageable pageable,Long clienteId);
    Page<SuscripcionBean>findAllByClienteIdAndEstadoAndActiveTrue(Pageable pageable,Long id,  EEstado estado);
    Page<SuscripcionBean>findAllByEstadoAndActiveTrue(Pageable pageable,EEstado estado);

    Optional< SuscripcionBean> findByClienteAndActividadAndEstadoAndFechaFinAfter(ClienteBean bean, ActividadBean bean1, EEstado eEstado, Date fechaInicio);
}
