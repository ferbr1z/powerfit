package com.devs.powerfit.exceptions;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException{
    public ConflictException(String mensaje) {
        super(HttpStatus.BAD_REQUEST, mensaje);
    }

    public ConflictException() {
        super(HttpStatus.BAD_REQUEST, "Existe un elemento con las mismos atributos");
    }
}
