package com.breno.cdd.casadocodigo.cadastronovoautor

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Unroll

import javax.persistence.EntityManager

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest([CadastroNovoAutorController])
@AutoConfigureJsonTesters
class CadastroNovoAutorControllerSpec extends Specification {
    @Autowired
    MockMvc mvc

    @SpringBean
    EntityManager em = Mock()

    def "Deve criar um novo autor"() {
        given:
        def requestData = [
                nome: "Fulano de Tal",
                email: "fufu@gmail.com",
                descricao: "Uma pessoa sem personalidade"]

        when:
        def result = mvc.perform(post("/autores")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(requestData)))
            .andReturn()

        then:
        result.response.status == HttpStatus.OK.value()

        and:
        !result.response.contentAsString

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
    def "Não deve criar um novo autor com os seguintes dados: nome: #nome, email: #email e descricao: #descricao"() {
        given:
            def requestData = [nome: nome, email: email, descricao: descricao]
        when:
        def response = mvc.perform(post("/autores")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(requestData)))
                .andReturn().response

        then:
        response.status == HttpStatus.BAD_REQUEST.value

        and :
        0 * em.persist(_ as Autor)

        where:
        nome            | email                     | descricao
        null            | 'fulano@gmail.com'         | 'Uma pessoa sem personalidade'
        'Fulano'        | null                      | 'Uma pessoa sem personalidade'
        'Fulano'        | 'fulano@gmail.com'        | null
        ''              | 'fulano@gmail.com'        | 'Uma pessoa sem personalidade'
        'Fulano'        | ''                        | 'Uma pessoa sem personalidade'
        'Fulano'        | 'fulano.com'              | 'Uma pessoa sem personalidade'
        'Fulano'        | 'fulano@gmail.com'        | ''
        'Fulano'        | 'fulano@gmail.com'        | 'Uma pessoa sem personalidade' * 20
    }
}
