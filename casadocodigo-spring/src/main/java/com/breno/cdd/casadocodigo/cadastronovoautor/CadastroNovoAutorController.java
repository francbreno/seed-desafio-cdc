package com.breno.cdd.casadocodigo.cadastronovoautor;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.validation.Valid;

@RestController
@RequestMapping(CadastroNovoAutorController.AUTORES_PATH)
@RequiredArgsConstructor
//Carga: 2
class CadastroNovoAutorController {
    public static final String AUTORES_PATH = "/autores";

    private final EntityManager em;

    @PostMapping
    @Transactional
    public void criarAutor(
            @Valid @RequestBody CadastroNovoAutorRequest requestData, // 1
            UriComponentsBuilder uriBuilder) {

        // 1
        Autor autor = new Autor(requestData.getNome(), requestData.getEmail(), requestData.getDescricao());
        em.persist(autor);
    }
}
