package com.devs.powerfit.daos.programas;

import com.devs.powerfit.beans.programas.ProgramaItemBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramaItemDao extends JpaRepository<ProgramaItemBean, Long> {
    Page<ProgramaItemBean> findAllByProgramaId(Pageable page, Long programaId);
    Optional<ProgramaItemBean> findByIdAndActiveTrue(Long itemId);

    @Query("SELECT pi FROM ProgramaItemBean pi WHERE pi.id = :itemId AND pi.programa.id = :programaId AND pi.active = true")
    Optional<ProgramaItemBean> findByIdAAndProgramaIdAndActiveTrue(Long itemId, Long programaId);

    List<ProgramaItemBean> findAllByProgramaId(Long programaId);

}
