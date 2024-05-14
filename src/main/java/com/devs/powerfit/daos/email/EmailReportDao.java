package com.devs.powerfit.daos.email;

import com.devs.powerfit.beans.email.EmailReportBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailReportDao extends JpaRepository<EmailReportBean, Long> {
    Optional<EmailReportBean> findFirstByOrderByIdAsc();

}
