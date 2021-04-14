package com.breno.cdd.casadocodigo

import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

import static io.restassured.RestAssured.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastroNovoAutorSpec extends Specification {
    @LocalServerPort
    private Integer serverPort

    private RequestSpecification requestSpec

    void setup() {
        requestSpec = RequestSpecBuilder
                .getDeclaredConstructor()
                .newInstance()
                .setPort(this.serverPort)
                .build()
    }

    def "Deve cadastrar novo autor quando são fornecidos dados válidos"() {
        given: "Eu quero cadastrar um novo autor"
        def novoAutor = [nome: "Fulano de Tal", email: "fufu@gmail.com", descricao: "Uma pessoa sem personalidade"]

        final request = given(this.requestSpec)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(novoAutor)

        when: "Faco um POST com os dados necessários para /autores"
        final response = request.post("/autores")

        then: "Devo receber um retorno com status OK"
        response.statusCode == HttpStatus.OK.value
    }
}
