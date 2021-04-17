package com.breno.cdd.casadocodigo.cadastrocategoria;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.validation.Valid;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
class CadastroCategoriaController {

    private final EntityManager em;

    @PostMapping
    @Transactional
    public void criarCategoria(@Valid @RequestBody CadastroCategoriaRequest requestData) {
        Categoria categoria = new Categoria(requestData.getNome());
        em.persist(categoria);
    }
}
