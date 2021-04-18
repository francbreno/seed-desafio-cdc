package com.breno.cdd.casadocodigo

import io.restassured.builder.RequestSpecBuilder
import io.restassured.specification.RequestSpecification
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

import java.time.LocalDate

import static io.restassured.RestAssured.given

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastroLivroSpec extends Specification {
    @LocalServerPort
    private Integer serverPort

    RequestSpecification requestSpec

    def setup() {
        this.requestSpec = RequestSpecBuilder
            .getDeclaredConstructor()
            .newInstance()
            .setPort(this.serverPort)
            .build()
    }

    def "Deve cadastrar um livro quando são fornecidos dados válidos"() {
        given:
        def novoLivro = [
                titulo: "Neuromancer",
                resumo: "Neuromancer, de William Gibson, é um dos mais famosos romances do gênero cyberpunk, e ganhou os três principais prêmios da ficção científica: Nebula, Hugo e Philip K. Dick, após sua publicação em 1984, tendo sido publicado em 1991 no Brasil pela editora Aleph",
                sumario: "O romance conta a história de Case, um ex-hacker (cowboy, como são chamados os hackers em ***Neuromancer***) que foi impossibilitado de exercer sua \"profissão\", graças a um erro que cometeu ao tentar roubar seus patrões. Eles então envenenaram Case com uma *micotoxina*, que danificou seu sistema neural e o impossibilitou de se conectar à ***Matrix***. Antes deixaram uma quantia de dinheiro com ele, pois *\"iria precisar dele\"*.",
                preco: 31.99,
                paginas: 320,
                isbn: "8576573008",
                dataPublicacao: LocalDate.now().plusDays(1).format("yyyy-MM-dd"),
                idCategoria: 1,
                idAutor: 1
        ]

        def request = given(this.requestSpec)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(novoLivro)

        when:
        def response = request.post("/livros");

        then:
        response.statusCode == HttpStatus.OK.value
    }
}
