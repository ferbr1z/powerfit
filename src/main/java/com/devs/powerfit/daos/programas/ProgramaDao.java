package com.devs.powerfit.daos.programas;

import com.devs.powerfit.beans.programas.ProgramaBean;

import com.devs.powerfit.dtos.programas.ProgramaForListDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramaDao extends JpaRepository<ProgramaBean, Long> {
    @Query("SELECT NEW com.devs.powerfit.dtos.programas.ProgramaForListDto(p.id, p.active, p.titulo, p.nivel, p.sexo, a.nombre, e.nombre) " +
            "FROM ProgramaBean p " +
            "JOIN p.actividad a " +
            "JOIN p.empleado e " +
            "WHERE " +
            "(:titulo IS NULL OR LOWER(p.titulo) LIKE CONCAT('%', LOWER(:titulo), '%')) AND " +
            "(:nivel IS NULL OR p.nivel = :nivel) AND " +
            "(:sexo IS NULL OR p.sexo = :sexo) AND " +
            "(p.active = true)")
    Page<ProgramaForListDto> findAll(@Param("titulo") String titulo,
                                     @Param("nivel") ENivelPrograma nivel,
                                     @Param("sexo") ESexo sexo,
                                     Pageable pageable);


    Optional<ProgramaBean> findByIdAndActiveTrue(Long id);
}
