package com.devs.powerfit.controllers.programas;

import com.devs.powerfit.dtos.programas.ProgramaDto;
import com.devs.powerfit.dtos.programas.ProgramaForListDto;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.interfaces.programas.IProgramaService;
import com.devs.powerfit.utils.responses.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/programas")
public class ProgramaController {
    private IProgramaService _service;
    @Autowired
    public ProgramaController(IProgramaService service){
        _service = service;
    }

    @PostMapping()
    public  ResponseEntity<ProgramaDto>  create(@RequestBody ProgramaDto programaDto){
        var newProgramaDto  = _service.create(programaDto);
        return new ResponseEntity<>(newProgramaDto, HttpStatus.CREATED);
    }
    

    @GetMapping("/page/{page}")
    ResponseEntity<PageResponse<ProgramaForListDto>> getAll(
            @PathVariable int page,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String nivel,
            @RequestParam(required = false) String sexo
    ) {
        ENivelPrograma eNivel = null;
        ESexo eSexo = null;

        try{
            if(nivel !=null)
                eNivel = ENivelPrograma.valueOf(nivel.toUpperCase());
            if(sexo!=null)
                eSexo = ESexo.valueOf(sexo.toUpperCase());
        } catch (Exception e){
        }

        var programas = _service.getAll(page, titulo, eNivel, eSexo);
        return new ResponseEntity<>(programas, HttpStatus.OK);
    }


    }
