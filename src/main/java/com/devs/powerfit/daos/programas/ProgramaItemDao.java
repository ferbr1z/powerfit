package com.devs.powerfit.daos.programas;

import com.devs.powerfit.beans.programas.ProgramaItemBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgramaItemDao extends JpaRepository<ProgramaItemBean, Long> {
    Page<ProgramaItemBean> findAllByProgramaId(Pageable page, Long programaId);
    Optional<ProgramaItemBean> findByIdAndActiveTrue(Long programaId, Long itemId);
}
