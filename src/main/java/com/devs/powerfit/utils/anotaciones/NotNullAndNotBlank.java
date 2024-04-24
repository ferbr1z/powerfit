package com.devs.powerfit.utils.anotaciones;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Documented
@Constraint(validatedBy = {})
@NotNull(message = "El campo no puede ser null")
@NotBlank(message = "El campo no puede estar en blanco")
@Size
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
/*
No acepta valores Nulos ni valores en blanco
 */
public @interface NotNullAndNotBlank {
        String message() default "El campo es inválido";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

}
