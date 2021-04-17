package com.breno.cdd.casadocodigo.cadastrocategoria;

import com.breno.cdd.casadocodigo.compartilhado.UniqueValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Value;
import org.springframework.util.Assert;

import javax.validation.constraints.NotBlank;


@Value
class CadastroCategoriaRequest {
    @NotBlank
    @UniqueValue(domainClass = Categoria.class, property = "nome")
    private final String nome;

    @JsonCreator
    public CadastroCategoriaRequest(@NotBlank String nome) {
        Assert.notNull(nome, "Nome não pode ser nulo");

        this.nome = nome;
    }
}
