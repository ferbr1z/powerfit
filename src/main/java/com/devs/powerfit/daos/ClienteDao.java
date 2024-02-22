package com.devs.powerfit.daos;

import com.devs.powerfit.beans.ClienteBean;
import com.devs.powerfit.dtos.ClienteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ClienteDao extends JpaRepository<ClienteBean, ClienteDto> {
    Optional<ClienteBean> findByIdAndActiveTrue(Long id);
    Page<ClienteBean> findByNombreIgnoreCaseContaining(String nombre, Pageable pageable);
    Page<ClienteBean> findByRucContaining(String nombre, Pageable pag);
    Boolean existsByIdAndActiveTrue(Long id);
    Page<ClienteBean> findAllByActiveTrue(Pageable pageable);
}
