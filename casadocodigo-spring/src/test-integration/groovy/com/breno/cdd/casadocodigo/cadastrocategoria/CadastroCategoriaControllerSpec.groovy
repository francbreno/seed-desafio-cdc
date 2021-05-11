package com.breno.cdd.casadocodigo.cadastrocategoria

import com.breno.cdd.casadocodigo.ApiExceptionHandler
import com.breno.cdd.casadocodigo.MockResponseReader
import com.fasterxml.jackson.databind.ObjectMapper
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest([CadastroCategoriaController])
class CadastroCategoriaControllerSpec extends Specification {

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    EntityManager em = Mock()

    @SpringBean
    JdbcTemplate jdbcTemplate = Mock()

    Map<String, Object> requestData = [nome: "Sci-fi"]
    MockResponseReader mockResponseReader

    def setup() {
        mockResponseReader = new MockResponseReader(objectMapper)
    }

    def "Deve criar uma categoria"() {
        given:
        jdbcTemplate.queryForList({it.contains("nome")}, Integer, requestData.nome) >> []

        when:
        def response = performPostToCategorias(requestData)

        then:
        response.status == HttpStatus.OK.value
        response.contentLength == 0

        and:
        1 * em.persist({
            verifyAll(it, Categoria) {
                nome == requestData.nome
            }
        })
    }

    def "Não deve criar uma Categoria com nome em branco"() {
        given:
        requestData = [nome: ""]

        and:
        jdbcTemplate.queryForList({it.contains("nome")}, Integer, requestData.nome) >> []

        when:
        def response = performPostToCategorias(requestData)

        then:
        response.status == HttpStatus.BAD_REQUEST.value

        and:
        with (mockResponseReader.read(response)) {
            message == ApiExceptionHandler.VALIDATION_ERROR_MESSAGE
            errors[0].field == "nome"
            errors[0].message == "must not be blank"
        }

        and: "O comportamento precisa ser definido já que a validaćão de unique será executada mesmo que com nome em branco"
        0 * em.persist(_ as Categoria)
    }

    @Unroll
    def "Não deve criar uma Categoria #testCase"() {
        when:
        def response = performPostToCategorias(data)

        then:
        response.status == HttpStatus.BAD_REQUEST.value

        and:
        with (mockResponseReader.read(response)) {
            message == errorMessage
        }

        and :
        0 * em.persist(_ as Categoria)

        where:
        testCase                    |  data             |  errorMessage
        "com nome nulo "            |  [nome: null]     |  ApiExceptionHandler.INVALID_DATA_ERROR_MESSAGE
        "sem dados necessários"     |  [:]              |  ApiExceptionHandler.INVALID_DATA_ERROR_MESSAGE
    }

    def "Não deve criar categoria com nome de uma categoria que já existe"() {
        given:
        jdbcTemplate.queryForList({it.contains("nome")}, Integer, requestData.nome) >> [new Categoria(requestData.nome)]

        when:
        def response = performPostToCategorias(requestData)

        then:
        response.status == HttpStatus.BAD_REQUEST.value

        and:
            with (mockResponseReader.read(response)) {
                message == ApiExceptionHandler.VALIDATION_ERROR_MESSAGE
                errors[0].field == "nome"
                errors[0].message == "Já existe um(a) Categoria com nome ${this.requestData.nome}"
            }

        and:
        0 * em.persist(_ as Categoria)
    }

    private MockHttpServletResponse performPostToCategorias(Map requestData) {
        mvc.perform(MockMvcRequestBuilders.post("/categorias")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(requestData)))
                .andReturn().response
    }
}
