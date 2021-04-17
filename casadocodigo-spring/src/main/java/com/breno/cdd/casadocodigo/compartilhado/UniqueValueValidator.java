package com.breno.cdd.casadocodigo.compartilhado;

import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@RequiredArgsConstructor
public class UniqueValueValidator implements ConstraintValidator<UniqueValue, Object> {
    private String property;
    private Class<?> klass;

    private final EntityManager em;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        List<?> resultList = em.createQuery("select 1 from "+ this.klass.getName() + " where " + this.property + "= :value")
                .setParameter("value", value)
                .getResultList();

        Assert.state(resultList.size() <= 1, "Mais de um " + this.klass + " foi encontrado com o valor " + value);

        return resultList.isEmpty();
    }

    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        this.property = constraintAnnotation.property();
        this.klass = constraintAnnotation.domainClass();
    }
}
