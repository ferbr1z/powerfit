package com.devs.powerfit.daos.clientes;

import com.devs.powerfit.beans.clientes.ClienteBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
@Repository
public interface ClienteDao extends JpaRepository<ClienteBean, Long> {
    Optional<ClienteBean> findByIdAndActiveTrue(Long id);
    Page<ClienteBean> findAllByRucAndActiveTrue(String ruc,Pageable pageable);
    Optional<ClienteBean> findByCedula( String cedula);
    Optional<ClienteBean> findByEmail( String email);
    Long countByFechaRegistroBetweenAndActiveTrue(LocalDate fechaInicio, LocalDate fechaFin);

    Page<ClienteBean> findAllByNombreContainingIgnoreCaseAndActiveIsTrue(Pageable pageable, String nombre);
    Page<ClienteBean>findByCedulaAndActiveIsTrue(Pageable pageable, String cedula);

    Page<ClienteBean> findAllByActiveTrue(Pageable pageable);
    List<ClienteBean> findAllByActiveTrue();
    Long countClientesByFechaRegistroBetween(LocalDate startOfMonth, LocalDate endOfMonth);
}
