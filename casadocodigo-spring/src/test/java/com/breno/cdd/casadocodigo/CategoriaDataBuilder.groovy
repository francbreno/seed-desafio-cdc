package com.breno.cdd.casadocodigo

import com.breno.cdd.casadocodigo.cadastrocategoria.Categoria

class CategoriaDataBuilder {
    private String nome

    CategoriaDataBuilder() {
        this.nome = "Sci-fi"
    }

    Categoria cria() {
        return new Categoria(this.nome);
    }

    CategoriaDataBuilder comNome(String nome) {
        this.nome = nome
        return this
    }

    CategoriaDataBuilder comNomeEmBranco() {
        this.nome = ""
        return this
    }
}
