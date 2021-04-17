package com.breno.cdd.casadocodigo.compartilhado;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UniqueValueValidator.class)
public @interface UniqueValue {
    String property();
    Class<?> domainClass();
    String message() default "{com.breno.cdd.casadocodigo.validation.unique}";
    Class<? extends Payload>[] payload() default {};
    Class<?>[] groups() default {};
}
