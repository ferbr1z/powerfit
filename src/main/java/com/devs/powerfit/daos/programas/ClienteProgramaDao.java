package com.devs.powerfit.daos.programas;

import com.devs.powerfit.beans.programas.ClienteProgramaBean;
import com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteProgramaDao extends JpaRepository<ClienteProgramaBean, Long> {

    @Query("SELECT cp FROM ClienteProgramaBean cp WHERE cp.programa.id = :programaId AND cp.id = :id AND cp.active = true")
    Optional<ClienteProgramaBean> findByProgramaIdAndId(@Param("programaId") Long programaId, @Param("id") Long id);


    @Query("SELECT NEW com.devs.powerfit.dtos.programas.clientePrograma.ClienteProgramaDto(cp.id, cp.active, p.id, p.titulo, c.id, c.nombre, cp.fechaEvaluacion) " +
            "FROM ClienteProgramaBean cp " +
            "JOIN cp.cliente c " +
            "JOIN cp.programa p " +
            "WHERE p.id = :programaId AND cp.active = true")
    Page<ClienteProgramaDto> findAllByProgramaId(@Param("programaId") Long programaId, Pageable page);
}
