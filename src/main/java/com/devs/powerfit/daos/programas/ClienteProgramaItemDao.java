package com.devs.powerfit.daos.programas;

import com.devs.powerfit.beans.programas.ClienteProgramaItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteProgramaItemDao extends JpaRepository<ClienteProgramaItem, Long> {

    @Query("SELECT cpi FROM ClienteProgramaItem cpi " +
            "WHERE cpi.clientePrograma.programa.id = :programaId " +
            "AND cpi.clientePrograma.id = :clienteProgramaId " +
            "AND cpi.id = :id " +
            "AND cpi.active = true")
    Optional<ClienteProgramaItem> findByIdCustom(@Param("programaId") Long programaId,
                                                @Param("clienteProgramaId") Long clienteProgramaId,
                                                @Param("id") Long id);
    @Query("SELECT cpi FROM ClienteProgramaItem cpi " +
            "WHERE cpi.clientePrograma.programa.id = :programaId " +
            "AND cpi.clientePrograma.id = :clienteProgramaId " +
            "AND cpi.active = true")
    Page<ClienteProgramaItem> findAllCustom(@Param("programaId") Long programaId, @Param("clienteProgramaId") Long clienteProgramaId, Pageable pageable);
}
