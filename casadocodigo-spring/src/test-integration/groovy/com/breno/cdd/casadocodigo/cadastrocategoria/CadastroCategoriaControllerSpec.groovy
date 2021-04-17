package com.breno.cdd.casadocodigo.cadastrocategoria

import com.breno.cdd.casadocodigo.cadastronovoautor.CadastroNovoAutorController
import com.breno.cdd.casadocodigo.cadastronovoautor.CadastroNovoAutorRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager
import javax.persistence.Query

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest([CadastroCategoriaController])
class CadastroCategoriaControllerSpec extends Specification {

    @Autowired
    MockMvc mvc;

    @SpringBean
    EntityManager em = Mock();

    Query query = Mock()

    def "Deve criar uma categoria"() {
        given:
        def requestData = [nome: "Sci-fi"];

        query.getResultList() >> []
        query.setParameter("value", requestData.nome) >> query
        em.createQuery(_ as String) >> query

        when:
        def response = mvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(requestData)))
                .andReturn().response

        then:
        response.status == HttpStatus.OK.value

        and:
        !response.contentAsString

        and:
        1 * em.persist({
            verifyAll(it, Categoria) {
                nome == requestData.nome
            }
        })
    }

    @Unroll
    def "Nào deve criar uma Categoria com nome inválido: nome: #nome"() {
        given:
        def requestData = [nome: nome]

        query.getResultList() >> []
        query.setParameter("value", requestData.nome) >> query
        em.createQuery(_ as String) >> query

        when:
        def response = mvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(requestData)))
                .andReturn().response

        then:
        response.status == responseStatus

        where:
        nome    || responseStatus
        null    || HttpStatus.BAD_REQUEST.value
        ""      || HttpStatus.BAD_REQUEST.value
    }
}
