package com.breno.cdd.casadocodigo.cadastronovoautor

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
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest([CadastroNovoAutorController])
class CadastroNovoAutorControllerSpec extends Specification {
    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    EntityManager em = Mock()

    @SpringBean
    JdbcTemplate jdbcTemplate = Mock()

    MockResponseReader mockResponseReader

    def setup() {
        mockResponseReader = new MockResponseReader(objectMapper)
    }

    def "Deve criar um novo autor"() {
        given:
        def requestData = [
                nome: "Fulano de Tal",
                email: "fufu@gmail.com",
                descricao: "Uma pessoa sem personalidade"]

        and:
        jdbcTemplate.queryForList({it.contains("email")}, Integer, requestData.email) >> []

        when:
        def response = performPostToAutores(requestData)

        then:
        response.status == HttpStatus.OK.value()

        and:
        !response.contentAsString

        and:
        1 * em.persist({
            verifyAll(it, Autor) {
                nome == requestData.nome
                email == requestData.email
                descricao == requestData.descricao
            }
        })
    }

    @Unroll
    def "Não deve criar um novo autor com #errorField inválido(a)"() {
        given:
        def requestData = [nome: nome, email: email, descricao: descricao]

        and:
        jdbcTemplate.queryForList({it.contains("email")}, Integer, requestData.email) >> []

        when:
        def response = performPostToAutores(requestData)

        then:
        response.status == HttpStatus.BAD_REQUEST.value

        and:
        with (mockResponseReader.read(response)) {
            message == com.breno.cdd.casadocodigo.ApiExceptionHandler.VALIDATION_ERROR_MESSAGE
            errors[0].field == errorField
            errors[0].message == errorMessage
        }

        and :
        0 * em.persist(_ as Autor)

        where:
        nome            | email                     | descricao                             | errorField    | errorMessage
        ''              | 'fulano@gmail.com'        | 'Uma pessoa sem personalidade'        | "nome"        | "must not be blank"
        'Fulano'        | ''                        | 'Uma pessoa sem personalidade'        | "email"       | "must not be blank"
        'Fulano'        | 'fulano.com'              | 'Uma pessoa sem personalidade'        | "email"       | "must be a well-formed email address"
        'Fulano'        | 'fulano@gmail.com'        | ''                                    | "descricao"   | "must not be blank"
        'Fulano'        | 'fulano@gmail.com'        | 'Uma pessoa sem personalidade' * 20   | "descricao"   | "size must be between 0 and 400"
    }

    @Unroll
    def "Não deve criar um novo autor com #errorField nulo"() {
        given:
        def requestData = [nome: nome, email: email, descricao: descricao]

        when:
        def response = performPostToAutores(requestData)

        then:
        response.status == HttpStatus.BAD_REQUEST.value

        and:
        with (mockResponseReader.read(response)) {
            message == com.breno.cdd.casadocodigo.ApiExceptionHandler.INVALID_DATA_ERROR_MESSAGE
        }

        and :
        0 * em.persist(_ as Autor)

        where:
        nome            | email                     | descricao                             | errorField
        null            | 'fulano@gmail.com'        | 'Uma pessoa sem personalidade'        | "nome"
        'Fulano'        | null                      | 'Uma pessoa sem personalidade'        | "email"
        'Fulano'        | 'fulano@gmail.com'        | null                                  | "descricao"
    }

    def "Não deve criar um novo autor sem fornecer os campos necessários"() {
        when:
        def response = performPostToAutores(requestData)

        then:
        response.status == HttpStatus.BAD_REQUEST.value

        and:
        with (mockResponseReader.read(response)) {
            message == com.breno.cdd.casadocodigo.ApiExceptionHandler.INVALID_DATA_ERROR_MESSAGE
        }

        and :
        0 * em.persist(_ as Autor)

        where:
        requestData << [
            [:],
            [email: "fulano@gmail.com", descricao: 'Uma pessoa sem personalidade'],
            [nome: "Fulano", descricao: 'Uma pessoa sem personalidade'],
            [nome: "Fulano", email: "fulano@gmail.com"],
        ]
    }

    private MockHttpServletResponse performPostToAutores(LinkedHashMap<String, String> requestData) {
        mvc.perform(post("/autores")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(requestData)))
                .andReturn().response
    }
}
