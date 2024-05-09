package com.devs.powerfit.utils;


import com.devs.powerfit.beans.auth.RolBean;
import com.devs.powerfit.beans.tipoDePagos.TipoDePagoBean;
import com.devs.powerfit.daos.auth.RolDao;
import com.devs.powerfit.daos.empleados.EmpleadoDao;
import com.devs.powerfit.daos.tiposDePago.TipoDePagoDao;
import com.devs.powerfit.dtos.empleados.EmpleadoDto;
import com.devs.powerfit.interfaces.empleados.IEmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolDao rolDao;
    private final TipoDePagoDao tipoDePagoDao;

    private final EmpleadoDao empleadoDao;

    private final IEmpleadoService empleadoService;

    @Autowired
    public DataInitializer(RolDao rolDao, TipoDePagoDao tipoDePagoDao, EmpleadoDao empleadoDao, IEmpleadoService empleadoService) {
        this.rolDao = rolDao;
        this.tipoDePagoDao = tipoDePagoDao;
        this.empleadoDao = empleadoDao;
        this.empleadoService = empleadoService;
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
        if (empleadoDao.findByEmailAndActiveIsTrue("admindefinitivo@gmail.com").isEmpty() || empleadoDao.count() == 0){
            EmpleadoDto empleado = new EmpleadoDto();
            empleado.setActive(true);
            empleado.setTelefono("0987123456");
            empleado.setEmail("admindefinitivo@gmail.com");
            empleado.setRol(1L);
            empleado.setCedula("12345678");
            empleado.setDireccion("Encarnación");
            empleado.setNombre("Administrador");
            empleadoService.create(empleado);
        }
        if (tipoDePagoDao.count() == 0){
            TipoDePagoBean pagoEfectivo = new TipoDePagoBean();
            pagoEfectivo.setActive(true);
            pagoEfectivo.setNombre("Efectivo");
            pagoEfectivo.setDescripcion("Pago en Efectivo");
            tipoDePagoDao.save(pagoEfectivo);
            TipoDePagoBean pagoTarjeta = new TipoDePagoBean();
            pagoTarjeta.setActive(true);
            pagoTarjeta.setNombre("Tarjeta");
            pagoTarjeta.setDescripcion("Pago con Tarjeta");
            tipoDePagoDao.save(pagoTarjeta);
            TipoDePagoBean pagoTransferencia = new TipoDePagoBean();
            pagoTransferencia.setActive(true);
            pagoTransferencia.setNombre("Transferencia");
            pagoTransferencia.setDescripcion("Pago con Transferencia");
            tipoDePagoDao.save(pagoTransferencia);
        }
    }

}