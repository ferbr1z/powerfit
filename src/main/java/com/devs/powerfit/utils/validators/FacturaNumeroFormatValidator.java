package com.devs.powerfit.utils.validators;

import com.devs.powerfit.utils.anotaciones.FacturaNumeroFormat;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FacturaNumeroFormatValidator implements ConstraintValidator<FacturaNumeroFormat, String> {

    private static final String FACTURA_REGEX = "\\d{3}-\\d{3}-\\d{8}";

    @Override
    public void initialize(FacturaNumeroFormat facturaNumeroFormat) {
    }

    @Override
    public boolean isValid(String facturaNumero, ConstraintValidatorContext context) {
        return facturaNumero == null || facturaNumero.matches(FACTURA_REGEX);
    }
}
