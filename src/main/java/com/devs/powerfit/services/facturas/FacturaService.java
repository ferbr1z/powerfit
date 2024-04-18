package com.devs.powerfit.services.facturas;

import com.devs.powerfit.beans.auth.UsuarioBean;
import com.devs.powerfit.beans.cajas.CajaBean;
import com.devs.powerfit.beans.cajas.SesionCajaBean;
import com.devs.powerfit.beans.facturas.FacturaBean;
import com.devs.powerfit.beans.facturas.FacturaDetalleBean;
import com.devs.powerfit.beans.suscripciones.SuscripcionBean;
import com.devs.powerfit.daos.auth.UsuarioDao;
import com.devs.powerfit.daos.cajas.CajaDao;
import com.devs.powerfit.daos.cajas.SesionCajaDao;
import com.devs.powerfit.daos.facturas.FacturaDao;
import com.devs.powerfit.daos.facturas.FacturaDetalleDao;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.facturas.FacturaDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.facturas.IFacturaService;
import com.devs.powerfit.services.clientes.ClienteService;
import com.devs.powerfit.services.suscripciones.SuscripcionService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.facturaMappers.FacturaMapper;
import com.devs.powerfit.utils.mappers.suscipcioneMapper.SuscripcionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FacturaService implements IFacturaService {
    private final FacturaDao facturaDao;
    private final FacturaMapper mapper;
    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;
    private final SesionCajaDao sesionCajaDao;
    private final CajaDao cajaDao;
    private final FacturaDetalleDao detalleDao;
    private final SuscripcionService suscripcionService;
    private final SuscripcionMapper suscripcionMapper;
    private final UsuarioDao usuarioDao;
    @Autowired
    public FacturaService(FacturaDao facturaDao, FacturaMapper mapper, ClienteService clienteService, ClienteMapper clienteMapper, SesionCajaDao sesionCajaDao, CajaDao cajaDao, FacturaDetalleDao detalleDao, SuscripcionService suscripcionService, SuscripcionMapper suscripcionMapper, UsuarioDao usuarioDao) {
        this.facturaDao = facturaDao;
        this.mapper = mapper;
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
        this.sesionCajaDao = sesionCajaDao;
        this.cajaDao = cajaDao;
        this.detalleDao = detalleDao;
        this.suscripcionService = suscripcionService;
        this.suscripcionMapper = suscripcionMapper;
        this.usuarioDao = usuarioDao;
    }
    @Override
    public FacturaDto create(FacturaDto facturaDto) {
        if ( facturaDto.getTimbrado() == null || facturaDto.getSesionId() == null || facturaDto.getTotal() == null) {
            throw new BadRequestException("Los campos timbrado, SesionId y total son obligatorios para crear una nueva factura");
        }
        ClienteDto clienteDto=null;
        if(facturaDto.getClienteId()!=null){
          clienteDto=clienteService.getById(facturaDto.getClienteId());
            if (clienteDto == null) {
                throw new NotFoundException("El cliente con ID " + facturaDto.getClienteId() + " no existe");
            }
        }
        Optional<SesionCajaBean> sesionOptional = sesionCajaDao.findByIdAndActiveTrue(facturaDto.getSesionId());
        if (sesionOptional.isEmpty()) {
            throw new BadRequestException("No existe sesión con ese ID");
        }
        SesionCajaBean sesion = sesionOptional.get();

        // Calcular el ivaTotal si no se proporciona explícitamente
        double ivaTotal = facturaDto.getIvaTotal() != null ? facturaDto.getIvaTotal() : facturaDto.getIva5() + facturaDto.getIva10();

//        // Verificar si los datos de ivaTotal y total son correctos
//        if (facturaDto.getIvaTotal() != null && facturaDto.getIvaTotal() != ivaTotal) {
//            throw new BadRequestException("El valor de ivaTotal proporcionado no coincide con el cálculo");
//        }

//        double total =  facturaDto.getSubTotal() + ivaTotal;
//        if (facturaDto.getTotal() != total) {
//            throw new BadRequestException("El valor de total proporcionado no coincide con el cálculo");
//        }

        // Convertir la fecha de String a LocalDate
        LocalDate fecha = facturaDto.getFecha() != null ? facturaDto.getFecha() : LocalDate.now();


        // Crear una instancia de Factura desde FacturaDto
        FacturaBean factura = new FacturaBean();
        factura.setSesion(sesion);
        if (clienteDto==null){
            factura.setCliente(null);
            factura.setNombreCliente("Sin nombre");
            factura.setRucCliente("Sin ruc");
            factura.setDireccion("Sin direccion");
        }else {
            factura.setCliente(clienteMapper.toBean(clienteDto));
            factura.setNombreCliente(clienteDto.getNombre());
            factura.setRucCliente(clienteDto.getRuc());
            factura.setDireccion(clienteDto.getDireccion());
        }
        factura.setTimbrado(facturaDto.getTimbrado());
        factura.setNroFactura(obtenerNumeroFacturaCompleto(facturaDto.getSesionId()));
        factura.setFecha(fecha);
        factura.setTotal(facturaDto.getTotal());
        factura.setSubTotal(facturaDto.getSubTotal());
        factura.setSaldo(facturaDto.getSaldo());
        factura.setIva5(facturaDto.getIva5() != null ? facturaDto.getIva5() : 0.0);
        factura.setIva10(facturaDto.getIva10() != null ? facturaDto.getIva10() : 0.0);
        factura.setIvaTotal(ivaTotal);
        factura.setPagado(false);
        factura.setActive(true);
        factura.setNombreCaja(obtenerNombreDeCaja(sesion.getId()));
        factura.setNombreEmpleado(obtenerNombreDeEmpleado(sesion.getId()));

        // Guardar la factura en la base de datos
        FacturaBean savedFactura = facturaDao.save(factura);

        // Retornar la FacturaDto creada
        return mapper.toDto(savedFactura);
    }

    private String obtenerNombreDeCliente(Long clienteId) {
        var cliente= clienteService.getById(clienteId);
        return cliente.getNombre();
    }



    @Override
    public FacturaDto getById(Long id) {
        var factura = facturaDao.findByIdAndActiveTrue(id);
        if (factura.isPresent()) {
            return mapper.toDto(factura.get());
        }
        throw new NotFoundException("Factura no encontrada");
    }


    @Override
    public PageResponse<FacturaDto> getAll(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByActiveTrue(pageRequest);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }


    @Override
    public FacturaDto update(Long id, FacturaDto facturaDto) {
        // Verificar si la factura con el ID proporcionado existe
        FacturaBean existingFactura = facturaDao.findById(id)
                .orElseThrow(() -> new NotFoundException("La factura con ID " + id + " no existe"));
        // Verificar si los campos obligatorios no están incompletos
        if (facturaDto.getClienteId() == null || facturaDto.getTimbrado() == null || facturaDto.getNroFactura() == null || facturaDto.getTotal() == null) {
            throw new BadRequestException("Los campos clienteId, timbrado, nroFactura y total son obligatorios para actualizar una factura");
        }

        // Verificar si el cliente existe
        ClienteDto clienteDto = clienteService.getById(facturaDto.getClienteId());
        if (clienteDto == null) {
            throw new NotFoundException("El cliente con ID " + facturaDto.getClienteId() + " no existe");
        }
        // Verificar si la factura ya existe con el nuevo número de factura
        if (!existingFactura.getNroFactura().equals(facturaDto.getNroFactura()) && facturaDao.existsByNroFactura(facturaDto.getNroFactura())) {
            throw new BadRequestException("Ya existe una factura con el número " + facturaDto.getNroFactura());
        }

        // Comprobar que los valores no sean negativos
        if (facturaDto.getSubTotal() < 0 || facturaDto.getIva5() < 0 || facturaDto.getIva10() < 0 || facturaDto.getTotal() < 0) {
            throw new BadRequestException("Los valores subTotal, iva5, iva10 y total no pueden ser negativos");
        }

        // Calcular el ivaTotal si no se proporciona explícitamente
        double ivaTotal = facturaDto.getIvaTotal() != null ? facturaDto.getIvaTotal() : facturaDto.getIva5() + facturaDto.getIva10();

        // Verificar si los datos de ivaTotal y total son correctos
        if (facturaDto.getIvaTotal() != null && facturaDto.getIvaTotal() != ivaTotal) {
            throw new BadRequestException("El valor de ivaTotal proporcionado no coincide con el cálculo");
        }

        double total =  facturaDto.getSubTotal() + ivaTotal;

        if (facturaDto.getTotal() != total) {
            throw new BadRequestException("El valor de total proporcionado no coincide con el cálculo");
        }

        // Convertir la fecha de String a Date
        LocalDate fecha;
        fecha = facturaDto.getFecha() != null ? facturaDto.getFecha() : LocalDate.now();


        // Actualizar la factura con los nuevos valores
        existingFactura.setCliente(clienteMapper.toBean(clienteDto));
        existingFactura.setTimbrado(facturaDto.getTimbrado());
        existingFactura.setNroFactura(facturaDto.getNroFactura());
        existingFactura.setNombreCliente(facturaDto.getNombreCliente());
        existingFactura.setRucCliente(facturaDto.getRucCliente());
        existingFactura.setFecha(fecha);
        existingFactura.setTotal(total);
        existingFactura.setSubTotal(facturaDto.getSubTotal() != null ? facturaDto.getSubTotal() : total - ivaTotal);
        existingFactura.setSaldo(facturaDto.getSaldo() != null ? facturaDto.getSaldo() : 0.0);
        existingFactura.setIva5(facturaDto.getIva5() != null ? facturaDto.getIva5() : 0.0);
        existingFactura.setIva10(facturaDto.getIva10() != null ? facturaDto.getIva10() : 0.0);
        existingFactura.setIvaTotal(ivaTotal);
        existingFactura.setPagado(facturaDto.isPagado());


        // Guardar los cambios en la base de datos
        FacturaBean updatedFactura = facturaDao.save(existingFactura);

        // Retornar la FacturaDto actualizada
        return mapper.toDto(updatedFactura);
    }

    @Override
    public boolean delete(Long id) {
        var factura = facturaDao.findByIdAndActiveTrue(id);
        if (factura.isPresent()) {
            var facturaBean = factura.get();
            facturaBean.setActive(false);
            facturaDao.save(facturaBean);
            return true;
        }
        throw new NotFoundException("factura no encontrada");
    }

    @Override
    public PageResponse<FacturaDto> searchByNombreCliente(String nombre, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByNombreClienteContainingIgnoreCaseAndActiveIsTrue(pageRequest,nombre);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }
    public PageResponse<FacturaDto> searchByNombreClienteAndPagado(String nombre, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByNombreClienteContainingIgnoreCaseAndPagado(pageRequest,nombre,true);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }
    public PageResponse<FacturaDto> searchByNombreClienteAndPendiente(String nombre, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByNombreClienteContainingIgnoreCaseAndPagado(pageRequest,nombre,false);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }

    @Override
    public PageResponse<FacturaDto> searchByRucCliente(String ruc, int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByRucClienteIgnoreCaseAndActiveTrue(pageRequest,ruc);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }
    public PageResponse<FacturaDto> searchByPagado(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByPagado(pageRequest,true);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }
    public PageResponse<FacturaDto> searchByPendiente(int page) {
        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByPagado(pageRequest,false);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }

    @Override
    public FacturaDto searchByNumeroFactura(String numeroFactura) {
        var factura = facturaDao.findByNroFacturaIgnoreCaseAndActiveTrue(numeroFactura);
        if (factura.isPresent()) {
            return mapper.toDto(factura.get());
        }
        throw new NotFoundException("Factura no encontrada");
    }
    public boolean actualizarSuscripcion(Long idFactura) {
        // Verificar si la factura con el ID proporcionado existe
        FacturaBean factura = facturaDao.findByIdAndActiveTrue(idFactura)
                .orElseThrow(() -> new NotFoundException("La factura con ID " + idFactura + " no existe"));

        // Obtener los detalles de la factura
        List<FacturaDetalleBean> detalles = detalleDao.findAllByFacturaIdAndActiveTrue(factura.getId());

        // Iterar sobre cada detalle y actualizar el estado de la suscripción (si existe)
        for (FacturaDetalleBean detalle : detalles) {
            SuscripcionBean suscripcion = detalle.getSuscripcion();
            if (suscripcion != null) { // Verificar si existe una suscripción en el detalle
                detalle.setSuscripcion(suscripcionMapper.toBean(suscripcionService.actualizarEstado(suscripcion.getId())));
                detalleDao.save(detalle);
            }
        }
        return true;
    }

    public FacturaDto actualizarSaldo(Long id, double nuevoSaldo) {
        // Verificar si la factura con el ID proporcionado existe
        FacturaBean factura = facturaDao.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("La factura con ID " + id + " no existe"));
        // Actualizar el saldo de la factura
        factura.setSaldo(nuevoSaldo);
        if(nuevoSaldo==0){
            if(actualizarSuscripcion(id)){
                System.out.println("Se actualizo correctamente la suscripcion");
            }
        }
        // Guardar los cambios en la base de datos
        FacturaBean facturaActualizada = facturaDao.save(factura);

        // Retornar la factura actualizada
        return mapper.toDto(facturaActualizada);
    }

    public FacturaDto modificarPagado(Long id, boolean pagado) {
        // Verificar si la factura con el ID proporcionado existe
        FacturaBean factura = facturaDao.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new NotFoundException("La factura con ID " + id + " no existe"));
        // Actualizar el estado de pago de la factura
        factura.setPagado(pagado);
        // Guardar los cambios en la base de datos
        FacturaBean facturaActualizada = facturaDao.save(factura);
        // Retornar la factura actualizada
        return mapper.toDto(facturaActualizada);
    }
    private String obtenerNumeroFacturaCompleto(Long sesionId) {
        Optional<SesionCajaBean> sesionOptional = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesionOptional.isPresent()) {
            SesionCajaBean sesionBean = sesionOptional.get();
            Optional<CajaBean> cajaOptional = cajaDao.findByIdAndActiveTrue(sesionBean.getCaja().getId());
            if (cajaOptional.isPresent()) {
                CajaBean cajaBean = cajaOptional.get();
                Long numeroFactura = cajaBean.getNumeroFactura();
                if (numeroFactura == null) {
                    numeroFactura = 1L;
                } else {
                    numeroFactura++;
                }
                cajaBean.setNumeroFactura(numeroFactura);

                String sucursal = "001";
                String numeroCajaFormatted = String.format("%03d", cajaBean.getNumeroCaja());
                String numeroFacturaFormatted = String.format("%08d", numeroFactura);

                String numeroFacturaCompleto = sucursal + "-" + numeroCajaFormatted + "-" + numeroFacturaFormatted;
                cajaDao.save(cajaBean);

                return numeroFacturaCompleto;
            } else {
                throw new BadRequestException("La caja asociada a la sesión no fue encontrada.");
            }
        } else {
            throw new BadRequestException("La sesión no fue encontrada.");
        }
    }
    private String obtenerNombreDeCaja(Long sesionId) {
        Optional<SesionCajaBean> sesionOptional = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesionOptional.isPresent()) {
            SesionCajaBean sesionBean = sesionOptional.get();
            Optional<CajaBean> cajaOptional = cajaDao.findByIdAndActiveTrue(sesionBean.getCaja().getId());
            if (cajaOptional.isPresent()) {
                CajaBean cajaBean = cajaOptional.get();
                return cajaBean.getNombre();
            } else {
                throw new BadRequestException("La caja asociada a la sesión no fue encontrada.");
            }
        } else {
            throw new BadRequestException("La sesión no fue encontrada.");
        }
    }
    private String obtenerNombreDeEmpleado(Long sesionId) {
        Optional<SesionCajaBean> sesionOptional = sesionCajaDao.findByIdAndActiveTrue(sesionId);
        if (sesionOptional.isPresent()) {
            SesionCajaBean sesionBean = sesionOptional.get();
            Optional<UsuarioBean> cajaOptional = usuarioDao.findByIdAndActiveTrue(sesionBean.getUsuario().getId());
            if (cajaOptional.isPresent()) {
                UsuarioBean usuarioBean = cajaOptional.get();
                return usuarioBean.getNombre();
            } else {
                throw new BadRequestException("La caja asociada a la sesión no fue encontrada.");
            }
        } else {
            throw new BadRequestException("La sesión no fue encontrada.");
        }
    }
    public PageResponse<FacturaDto> searchByFecha(int page, LocalDate fechaInicio, LocalDate fechaFin) {
        // Validar que la fecha final sea igual o posterior a la fecha inicial
        if (fechaFin.isBefore(fechaInicio)) {
            throw new BadRequestException("La fecha final debe ser igual o posterior a la fecha inicial");
        }

        var pageRequest = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        var facturaPage = facturaDao.findAllByFechaBetween(pageRequest, fechaInicio, fechaFin);
        if (facturaPage.isEmpty()) {
            throw new NotFoundException("No hay facturas en la lista");
        }
        var facturaDtoPage = facturaPage.map(mapper::toDto);
        return new PageResponse<>(facturaDtoPage.getContent(),
                facturaDtoPage.getTotalPages(),
                facturaDtoPage.getTotalElements(),
                facturaDtoPage.getNumber() + 1);
    }



}