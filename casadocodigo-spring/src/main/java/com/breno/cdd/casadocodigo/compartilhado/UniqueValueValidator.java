package com.breno.cdd.casadocodigo.compartilhado;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.Assert;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@RequiredArgsConstructor
public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {
    private String property;
    private Class<?> klass;

    private final JdbcTemplate jdbcTemplate;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        List<Integer> resultList = jdbcTemplate.queryForList("select 1 from " + this.klass.getSimpleName() + " where " + this.property + "= ?", Integer.class, value);
        Assert.state(resultList.size() <= 1, "Mais de um(a) " + this.klass + " foi encontrado(a) com o valor " + value);

        return resultList.isEmpty();
    }

    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        this.property = constraintAnnotation.property();
        this.klass = constraintAnnotation.domainClass();
    }
}
