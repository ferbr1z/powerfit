package com.devs.powerfit.interfaces.arqueo;


import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import com.devs.powerfit.utils.responses.PageResponse;

public interface IArqueoService {
     public ArqueoDto realizarArqueo(Long sesionCajaId);

     public PageResponse<ArqueoDto> getAll(int page);

}
