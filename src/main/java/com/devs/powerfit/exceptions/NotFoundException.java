package com.devs.powerfit.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {

    public NotFoundException(String mensaje) {
        super(HttpStatus.NOT_FOUND, mensaje);
    }

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND, "Recurso no encontrado");
    }

}