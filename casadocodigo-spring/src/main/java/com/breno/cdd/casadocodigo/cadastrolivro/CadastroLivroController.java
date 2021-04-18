package com.breno.cdd.casadocodigo.cadastrolivro;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
class CadastroLivroController {

    private final EntityManager em;

    @PostMapping
    public void cadastrarLivro(@Valid @RequestBody CadastroLivroRequest requestData) {
        this.em.persist(requestData.toModel(em));
    }
}
