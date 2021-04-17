package com.breno.cdd.casadocodigo.cadastroCategoria

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import javax.persistence.EntityManager

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

class CadastroCategoriaControllerSpec extends Specification {

    @Autowired
    MockMvc mockMvc;

    @SpringBean
    EntityManager em = Mock();

    def "Deve criar uma categoria"() {
        given:
        def categoria = new Categoria("Sci-fi");

        when:
        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(toJson()))

        then:
    }
}
