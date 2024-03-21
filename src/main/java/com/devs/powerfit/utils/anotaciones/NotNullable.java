package com.devs.powerfit.utils.anotaciones;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@NotNull(message = "El campo no puede ser null")
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/*
No acepta valores Nulos
 */
public @interface NotNullable{
    String message() default "El campo es inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
