package com.devs.powerfit.daos;

import com.devs.powerfit.beans.ClienteBean;
import com.devs.powerfit.dtos.ClienteDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteDao extends JpaRepository<ClienteBean, ClienteDto> {
}
