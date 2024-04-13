package com.devs.powerfit.daos.suscripciones;

import com.devs.powerfit.beans.actividades.ActividadBean;
import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.enums.EEstado;
import com.devs.powerfit.enums.EModalidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuscripcionDao extends JpaRepository<SuscripcionBean,Long>{
    Optional<SuscripcionBean> findByIdAndActiveTrue(Long id);
    Page<SuscripcionBean> findAllByActiveTrue(Pageable pageable);
    Page<SuscripcionBean> findAllByClienteIdAndActiveTrue(Pageable pageable,Long clienteId);
    Page<SuscripcionBean>findAllByClienteIdAndEstadoAndActiveTrue(Pageable pageable,Long id,  EEstado estado);
    Page<SuscripcionBean>findAllByEstadoAndActiveTrue(Pageable pageable,EEstado estado);
    boolean existsByClienteAndActividadAndEstado(ClienteBean cliente,ActividadBean actividad,EEstado estado);
    Long countDistinctByActividadAndModalidadAndActiveTrue(ActividadBean actividad, EModalidad modalidad);
    Page<SuscripcionBean> findAllByActividadAndActiveTrue(ActividadBean actividad,Pageable pageable);
    boolean existsByClienteAndEstadoAndActiveTrue(ClienteBean cliente, EEstado estado);
    List<SuscripcionBean> findAllByEstadoAndActiveTrueAndFinalizadoFalse(EEstado estado);

    List<SuscripcionBean> findAllByActiveTrue();
    List<SuscripcionBean> findAllByActiveTrueAndFinalizadoFalse();
    Long countByClienteAndEstadoAndActiveTrue(ClienteBean cliente, EEstado estado);

    List<SuscripcionBean> findAllByActiveTrueAndModalidad(EModalidad modalidad);


}
