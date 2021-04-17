package com.breno.cdd.casadocodigo

import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

import static io.restassured.RestAssured.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastroCategoriaSpec extends Specification {
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

    def "Deve cadastrar uma categoria quando são fornecidos dados válidos"() {
        given: "Eu quero cadastrar uma categoria"
        def novaCategoria = [nome: "Sci-fi"]

        def request = given(this.requestSpec)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .body(novaCategoria)

        when: "Faco um POST com os dados necessários para /categorias"
        def response = request.post("/categorias")

        then: "Devo receber um retorno com status 200"
        response.statusCode == HttpStatus.OK.value
    }
}
