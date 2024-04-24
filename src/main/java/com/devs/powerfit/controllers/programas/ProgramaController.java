package com.devs.powerfit.controllers.programas;

import com.devs.powerfit.dtos.programas.*;
import com.devs.powerfit.enums.ENivelPrograma;
import com.devs.powerfit.enums.ESexo;
import com.devs.powerfit.exceptions.NotFoundException;
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
    public  ResponseEntity<ProgramaDto>  create(@RequestBody CrearAndUpdateProgramaDto programaDto){
        var newProgramaDto  = _service.create(programaDto);
        return new ResponseEntity<>(newProgramaDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullProgramaDto> getById(@PathVariable Long id){
        var programa = (FullProgramaDto) _service.getById(id);
        if(programa==null){
            throw new NotFoundException("Producto no encontrado");
        }
        return new ResponseEntity<>(programa, HttpStatus.OK);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<PageResponse<ProgramaForListDto>> getAll(
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

    @PutMapping("/{id}")
    public ResponseEntity<ProgramaDto> update(@PathVariable Long id, @RequestBody CrearAndUpdateProgramaDto updateDto) {
        var updated = _service.update(id, updateDto);
        if(updated==null){
            throw new NotFoundException("Producto no encontrado");
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id){
        return new ResponseEntity<>(_service.delete(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<ProgramaItemDto> addItem(@PathVariable Long id, @RequestBody ProgramaItemDto itemDto){
        var item = _service.createItem(id, itemDto);
        if(item==null){
            throw new NotFoundException("Producto no encontrado");
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping("/{id}/items/{itemId}")
    public ResponseEntity<ProgramaItemDto> getItemById(@PathVariable Long id, @PathVariable Long itemId){
        var item = _service.getItemById(id, itemId);
        if(item==null){
            throw new NotFoundException("Producto no encontrado");
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }


    @GetMapping("/{id}/items/page/{page}")
    public ResponseEntity<PageResponse<ProgramaItemDto>> getItemsByProgramaId(@PathVariable int page, @PathVariable Long id){
        var items = _service.getItemsByProgramaId(id, page);
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<ProgramaItemDto> updateItem(@PathVariable Long id, @PathVariable Long itemId, @RequestBody ProgramaItemDto itemDto){
        var item = _service.updateItem(id, itemId, itemDto);
        if(item==null){
            throw new NotFoundException("Producto no encontrado");
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Boolean> deleteItem(@PathVariable Long id, @PathVariable Long itemId){
        return new ResponseEntity<>(_service.deleteItem(id, itemId), HttpStatus.OK);
    }

}
