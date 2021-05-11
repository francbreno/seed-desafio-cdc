package com.breno.cdd.casadocodigo.compartilhado;

import lombok.Getter;

@Getter
public class AssociatedEntityNotFoundException extends RuntimeException {
    private Class<?> klass;
    private Object value;

    public AssociatedEntityNotFoundException(Class<?> klass, Object value) {
        super();
        this.klass = klass;
        this.value = value;
    }

    public String getMessage() {
        return String.format("Não foi encontrado(a) %s com id %s", this.klass.getSimpleName(), this.value);
    }
}
