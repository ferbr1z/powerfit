package com.devs.powerfit.utils;


import com.devs.powerfit.beans.auth.RolBean;
import com.devs.powerfit.daos.auth.RolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolDao rolDao;

    @Autowired
    public DataInitializer(RolDao rolDao) {
        this.rolDao = rolDao;
    }

    @Override
    public void run(String... args) throws Exception {
        if (rolDao.count() == 0) {
            RolBean rolAdmin = new RolBean();
            rolAdmin.setNombre("ADMIN");
            rolAdmin.setActive(true);
            rolDao.save(rolAdmin);
            RolBean rolUsuario = new RolBean();
            rolUsuario.setNombre("CLIENTE");
            rolUsuario.setActive(true);
            rolDao.save(rolUsuario);
            RolBean rolCajero = new RolBean();
            rolCajero.setNombre("CAJERO");
            rolCajero.setActive(true);
            rolDao.save(rolCajero);
            RolBean rolEntrenador = new RolBean();
            rolEntrenador.setNombre("ENTRENADOR");
            rolEntrenador.setActive(true);
            rolDao.save(rolEntrenador);


        }
    }
}