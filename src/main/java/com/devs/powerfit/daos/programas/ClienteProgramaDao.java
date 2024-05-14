package com.devs.powerfit.daos.programas;

import com.devs.powerfit.beans.programas.ClienteProgramaBean;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ClienteProgramaDao extends JpaRepository<ClienteProgramaBean, Long> {

    @Query("SELECT cp FROM ClienteProgramaBean cp " +
            "WHERE cp.programa.id = :programaId " +
            "AND cp.id = :id " +
            "AND cp.active = true")
    Optional<ClienteProgramaBean> findByProgramaIdAndId(@Param("programaId") Long programaId, @Param("id") Long id);

    @Query("SELECT NEW com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto(cp.id, cp.active, p.id, p.titulo, c.id, c.nombre, cp.fechaEvaluacion) " +
            "FROM ClienteProgramaBean cp " +
            "JOIN cp.cliente c " +
            "JOIN cp.programa p " +
            "WHERE p.id = :programaId " +
            "AND cp.active = true " +
            "AND c.active = true " +
            "AND (:nombreCliente IS NULL or LOWER(cp.cliente.nombre) LIKE CONCAT('%', LOWER(:nombreCliente), '%')) " +
            "AND (:fechaInicio IS NULL OR :fechaFin IS NULL OR cp.fechaEvaluacion BETWEEN :fechaInicio AND :fechaFin)")
    Page<ClienteProgramaDto> findAllByProgramaId(
            @Param("programaId") Long programaId,
            @Param("nombreCliente") String nombreCliente,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin,
            Pageable page);
}
