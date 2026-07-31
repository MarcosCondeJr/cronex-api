package com.chronex.cronex_api.common.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = CpfCnpjValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CpfCnpj {
    String message() default "Cpf ou Cnpj inválido";
    Class<?>[] groups() default {}; 
    Class<? extends Payload>[] payload() default {}; 
}
