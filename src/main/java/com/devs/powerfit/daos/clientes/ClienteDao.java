package com.devs.powerfit.daos.clientes;

import com.devs.powerfit.beans.clientes.ClienteBean;
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
    Optional<ClienteBean> findByEmail( String email);

    Page<ClienteBean> findAllByNombreContainingIgnoreCaseAndActiveIsTrue(Pageable pageable, String nombre);
    Page<ClienteBean>findByCedulaAndActiveIsTrue(Pageable pageable, String cedula);

    Page<ClienteBean> findAllByActiveTrue(Pageable pageable);
}
