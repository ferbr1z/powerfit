package com.devs.powerfit.daos.clientes;

import com.devs.powerfit.beans.clientes.ClienteBean;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClienteDao extends JpaRepository<ClienteBean, Long> {
    Optional<ClienteBean> findByIdAndActiveTrue(Long id);
    Optional<ClienteBean> findByRucAndCedula(String ruc, String cedula);
    Optional<ClienteBean> findByRucOrCedula(String ruc, String cedula);
    Optional<ClienteBean> findByCedula( String cedula);

    Page<ClienteBean> findByNombreAndActiveIsTrue(Pageable pageable, String nombre);
    Page<ClienteBean>findByCedulaAndActiveIsTrue(Pageable pageable, String cedula);

    Page<ClienteBean> findAllByActiveTrue(Pageable pageable);
}
