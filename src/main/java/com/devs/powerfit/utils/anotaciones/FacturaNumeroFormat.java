package com.devs.powerfit.utils.anotaciones;

import com.devs.powerfit.utils.validators.FacturaNumeroFormatValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FacturaNumeroFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FacturaNumeroFormat {
    String message() default "Número de factura inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
