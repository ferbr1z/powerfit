package com.devs.powerfit.utils.anotaciones;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.Size;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Size(min = 0)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/*
Acepta valores nulos, pero no en blanco
 */
public @interface NullableOrNotBlank {
    String message() default "El campo es inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
