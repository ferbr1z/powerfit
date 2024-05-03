package com.devs.powerfit.interfaces.arqueo;


import com.devs.powerfit.dtos.arqueo.ArqueoDto;
import com.devs.powerfit.dtos.arqueo.ArqueoRequestDto;
import com.devs.powerfit.utils.responses.PageResponse;

import java.time.LocalDate;
import java.util.Date;

public interface IArqueoService {
     public ArqueoDto realizarArqueo(ArqueoRequestDto arqueoRequestDto);

     public PageResponse<ArqueoDto> getAll(int page);

     PageResponse<ArqueoDto> getAllByFecha(LocalDate fecha, int page);
;

}
