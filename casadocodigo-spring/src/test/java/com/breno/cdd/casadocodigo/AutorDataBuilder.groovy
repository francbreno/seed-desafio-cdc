package com.breno.cdd.casadocodigo

import com.breno.cdd.casadocodigo.cadastronovoautor.Autor

class AutorDataBuilder {
    private String nome;
    private String email;
    private String descricao;

    AutorDataBuilder() {
        this.nome = "William Gibson"
        this.email = "wgibson@gmail.com"
        this.descricao = "Famoso autor"
    }

    Autor cria() {
        return new Autor(nome, email, descricao)
    }

    AutorDataBuilder comNome(String nome) {
        this.nome = nome
        return this
    }

    AutorDataBuilder comNomeEmBranc() {
        this.nome = ""
        return this
    }

    AutorDataBuilder comEmail(String email) {
        this.email = email
        return this
    }

    AutorDataBuilder comEmailEmBranco() {
        this.email = ""
        return this
    }

    AutorDataBuilder comDescricao(String descricao) {
        this.descricao = descricao
        return this
    }

    AutorDataBuilder comDescricaoEmBranco() {
        this.descricao = ""
        return this
    }
}
