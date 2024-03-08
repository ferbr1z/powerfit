package com.devs.powerfit.services.mediciones;

import com.devs.powerfit.beans.mediciones.MedicionBean;
import com.devs.powerfit.daos.mediciones.MedicionDao;
import com.devs.powerfit.dtos.clientes.ClienteDto;
import com.devs.powerfit.dtos.mediciones.MedicionDto;
import com.devs.powerfit.exceptions.BadRequestException;
import com.devs.powerfit.exceptions.NotFoundException;
import com.devs.powerfit.interfaces.mediciones.IMedicionService;
import com.devs.powerfit.services.clientes.ClienteService;
import com.devs.powerfit.utils.Setting;
import com.devs.powerfit.utils.mappers.clienteMappers.ClienteMapper;
import com.devs.powerfit.utils.mappers.medicionMappers.MedicionMapper;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicionService implements IMedicionService {
    private MedicionDao medicionDao;
    private ClienteService clienteService;
    private MedicionMapper mapper;
    private ClienteMapper clienteMapper;
    @Autowired
    public MedicionService(MedicionDao medicionDao, ClienteService clienteService,
                           MedicionMapper mapper, ClienteMapper clienteMapper){
        this.medicionDao = medicionDao;
        this.clienteService = clienteService;
        this.mapper = mapper;
        this.clienteMapper = clienteMapper;
    }
    @Override
    public MedicionDto create(MedicionDto medicionDto) {
        // Verificar si los campos obligatorios no están incompletos
        if (medicionDto.getClienteID() == null ||
                medicionDto.getFecha() == null ||
                medicionDto.getPeso() == null ||
                medicionDto.getAltura() == null) {
            throw new BadRequestException("Los campos clienteID, fecha, peso y altura son obligatorios para crear una nueva medición");
        }

        // Verificar si el cliente existe
        ClienteDto clienteDto = clienteService.getById(medicionDto.getClienteID());
        if (clienteDto == null) {
            throw new NotFoundException("El cliente especificado no existe");
        }

        // Obtener el peso y la altura
        double peso = medicionDto.getPeso();
        double altura = medicionDto.getAltura();

        // Verificar que el peso y la altura no sean cero ni negativos
        if (peso <= 0 || altura <= 0) {
            throw new BadRequestException("El peso y la altura deben ser valores positivos y no pueden ser cero");
        }

        // Verificar si el IMC es proporcionado o calcularlo
        Double imc = medicionDto.getImc();
        if (imc == null || imc == 0) {
            // Calcular IMC
            imc = peso / (altura * altura);
            // Redondear el IMC a dos decimales
            imc = Math.round(imc * 100.0) / 100.0;
        }

        // Verificar que el IMC no sea negativo
        if (imc < 0) {
            throw new BadRequestException("El IMC no puede ser un valor negativo");
        }

        // Crear una instancia de Medicion desde MedicionDto
        MedicionBean medicion = new MedicionBean();
        medicion.setCliente(clienteMapper.toBean(clienteDto));
        medicion.setFecha(medicionDto.getFecha());
        medicion.setPeso(peso);
        medicion.setAltura(altura);
        medicion.setImc(imc);

        // Establecer las circunferencias corporales si se proporcionan
        if (medicionDto.getCirBrazo() != null && medicionDto.getCirBrazo() < 0 ||
                medicionDto.getCirPiernas() != null && medicionDto.getCirPiernas() < 0 ||
                medicionDto.getCirCintura() != null && medicionDto.getCirCintura() < 0 ||
                medicionDto.getCirPecho() != null && medicionDto.getCirPecho() < 0) {
            throw new BadRequestException("Las circunferencias corporales deben ser valores positivos");
        }

        medicion.setCirBrazo(medicionDto.getCirBrazo());
        medicion.setCirPiernas(medicionDto.getCirPiernas());
        medicion.setCirCintura(medicionDto.getCirCintura());
        medicion.setCirPecho(medicionDto.getCirPecho());

        medicion.setActive(true);

        // Guardar la medicion en la base de datos
        MedicionBean savedMedicion = medicionDao.save(medicion);

        // Retornar el MedicionDao creado
        return mapper.toDto(savedMedicion);
    }


    @Override
    public MedicionDto getById(Long id) {
        var medicionOptional = medicionDao.findByIdAndActiveTrue(id);
        if(medicionOptional.isPresent()){
            var medicionBean = medicionOptional.get();
            return mapper.toDto(medicionBean);
        }
        throw new NotFoundException("Medición no encontrada" );
    }

    @Override
    public PageResponse<MedicionDto> getAll(int page) {
        var pag = PageRequest.of(page -1, Setting.PAGE_SIZE);
        var mediciones = medicionDao.findAllByActiveTrue(pag);

        if(mediciones.isEmpty()){
            throw new NotFoundException("No hay mediciones en la lista");
        }

        var medicionesDto = mediciones.map(medicion -> mapper.toDto(medicion));
        var pageResponse = new PageResponse<MedicionDto>(
                medicionesDto.getContent(),
                medicionesDto.getTotalPages(),
                medicionesDto.getTotalElements(),
                medicionesDto.getNumber() + 1
        );
        return pageResponse;
    }
    @Override
    public MedicionDto update(Long id, MedicionDto medicionDto) {
        // Verificar si la medición existe
        var medicionOptional = medicionDao.findByClienteIdAndActiveTrue(id);
        if(medicionOptional.isPresent()) {
            var medicionBean = medicionOptional.get();

            // Verificar y actualizar los campos de la medición con los valores del DTO
            if (medicionDto.getFecha() != null) {
                medicionBean.setFecha(medicionDto.getFecha());
            }

            if (medicionDto.getPeso() != null && medicionDto.getPeso() > 0) {
                medicionBean.setPeso(medicionDto.getPeso());
            } else {
                throw new BadRequestException("El peso debe ser un valor positivo");
            }

            if (medicionDto.getAltura() != null && medicionDto.getAltura() > 0) {
                medicionBean.setAltura(medicionDto.getAltura());
            } else {
                throw new BadRequestException("La altura debe ser un valor positivo");
            }

            if (medicionDto.getImc() != null && medicionDto.getImc() >= 0) {
                medicionBean.setImc(medicionDto.getImc());
            } else {
                throw new BadRequestException("El IMC debe ser un valor no negativo");
            }

            if (medicionDto.getCirBrazo() != null && medicionDto.getCirBrazo() > 0) {
                medicionBean.setCirBrazo(medicionDto.getCirBrazo());
            }

            if (medicionDto.getCirPiernas() != null && medicionDto.getCirPiernas() > 0) {
                medicionBean.setCirPiernas(medicionDto.getCirPiernas());
            }

            if (medicionDto.getCirCintura() != null && medicionDto.getCirCintura() > 0) {
                medicionBean.setCirCintura(medicionDto.getCirCintura());
            }

            if (medicionDto.getCirPecho() != null && medicionDto.getCirPecho() > 0) {
                medicionBean.setCirPecho(medicionDto.getCirPecho());
            }

            // Recalcular el IMC
            double peso = medicionBean.getPeso();
            double altura = medicionBean.getAltura();
            double imc = peso / (altura * altura);
            // Redondear el IMC a dos decimales
            imc = Math.round(imc * 100.0) / 100.0;
            medicionBean.setImc(imc);

            // Guardar la medición actualizada en la base de datos
            medicionDao.save(medicionBean);

            return mapper.toDto(medicionBean);
        }
        throw new NotFoundException("Medición no encontrada");
    }



    @Override
    public boolean delete(Long id) {
        var medicionOptional = medicionDao.findById(id);
        if(medicionOptional.isPresent()){
            var medicionBean = medicionOptional.get();
            // Desactivar la medicion en lugar de eliminarla físicamente
            medicionBean.setActive(false);
            medicionDao.save(medicionBean);
            return true;
        }
        throw new NotFoundException("Medición no encontrada");
    }

    @Override
    public PageResponse<MedicionDto> searchByNombreCliente(String nombre, int page) {
        // Buscar clientes por nombre utilizando el servicio de cliente
        PageResponse<ClienteDto> clientesResponse = clienteService.searchByNombre(nombre, page);
        List<ClienteDto> clientes = clientesResponse.getItems();

        if (clientes.isEmpty()) {
            throw new NotFoundException("No se encontraron clientes con ese nombre");
        }

        // Obtener mediciones para los clientes encontrados
        List<MedicionBean> mediciones = new ArrayList<>();
        clientes.forEach(cliente -> {
            Optional<MedicionBean> medicion = medicionDao.findByClienteIdAndActiveTrue(cliente.getId());
            medicion.ifPresent(mediciones::add);
        });

        if(mediciones.isEmpty()){
            throw new NotFoundException("No se encontraron mediciones para los clientes con ese nombre");
        }

        // Convertir las mediciones a DTOs
        List<MedicionDto> medicionesDto = mediciones.stream()
                .map(medicion -> mapper.toDto(medicion))
                .toList();
        // Crear y retornar la respuesta de la página
        return new PageResponse<>(medicionesDto, clientesResponse.getTotalPages(), clientesResponse.getTotalItems(), page);
    }

    @Override
    public PageResponse<MedicionDto> searchByCiCliente(int ci, int page) {
        // Buscar clientes por nombre utilizando el servicio de cliente
        PageResponse<ClienteDto> clientesResponse = clienteService.searchByCi(String.valueOf(ci), page);
        List<ClienteDto> clientes = clientesResponse.getItems();

        if (clientes.isEmpty()) {
            throw new NotFoundException("No se encontraron clientes con ese nombre");
        }

        // Obtener mediciones para los clientes encontrados
        List<MedicionBean> mediciones = new ArrayList<>();
        clientes.forEach(cliente -> {
            Optional<MedicionBean> medicion = medicionDao.findByClienteIdAndActiveTrue(cliente.getId());
            medicion.ifPresent(mediciones::add);
        });

        if(mediciones.isEmpty()){
            throw new NotFoundException("No se encontraron mediciones para los clientes con ese nombre");
        }

        // Convertir las mediciones a DTOs
        List<MedicionDto> medicionesDto = mediciones.stream()
                .map(medicion -> mapper.toDto(medicion))
                .toList();
        // Crear y retornar la respuesta de la página
        return new PageResponse<>(medicionesDto, clientesResponse.getTotalPages(), clientesResponse.getTotalItems(), page);
    }

    @Override
    public PageResponse<MedicionDto> searchByIdCliente(Long id, int page) {
        var pag = PageRequest.of(page - 1, Setting.PAGE_SIZE);
        // Buscar clientes por nombre utilizando el servicio de cliente
        ClienteDto cliente = clienteService.getById(id);

        if (cliente == null) {
            throw new NotFoundException("No se encontró cliente con ese id");
        }

        // Obtener mediciones para el cliente encontrado
        var medicionesResponse = medicionDao.findAllByClienteIdAndActiveTrue(pag, cliente.getId());

        if(medicionesResponse.isEmpty()){
            throw new NotFoundException("No se encontraron mediciones para el cliente con esa id");
        }

        // Convertir las mediciones a DTOs
        List<MedicionDto> medicionesDto = medicionesResponse.stream()
                .map(medicionBean -> mapper.toDto(medicionBean))
                .toList();
        // Crear y retornar la respuesta de la página
        return new PageResponse<>(medicionesDto, medicionesResponse.getTotalPages(), medicionesResponse.getTotalElements(), page);
    }
}
