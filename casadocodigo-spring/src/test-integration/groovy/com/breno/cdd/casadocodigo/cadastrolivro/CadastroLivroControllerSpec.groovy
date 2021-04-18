package com.breno.cdd.casadocodigo.cadastrolivro

import com.breno.cdd.casadocodigo.cadastrocategoria.Categoria
import com.breno.cdd.casadocodigo.cadastronovoautor.Autor
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest([CadastroLivroController])
class CadastroLivroControllerSpec extends Specification {
    @Autowired
    MockMvc mvc

    @SpringBean
    EntityManager em = Mock();

    Query query = Mock()

    def "Deve cadastrar um livro"() {
        given:
            def requestData = [
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

        def categoriaAssociada = new Categoria("Sci-fi");
        categoriaAssociada.setId(1)

        def autorAssociado = new Autor("Fulano", "fufu@gmail.com", "Uma pessoa")
        autorAssociado.setId(1)

        em.find(Categoria, 1L) >> categoriaAssociada
        em.find(Autor, 1L) >> autorAssociado

        query.getResultList() >> []
        query.setParameter("value", requestData.titulo) >> query
        em.createQuery(_ as String) >> query

        when:
        def response = mvc.perform(post("/livros")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(toJson(requestData)))
                    .andReturn().response

        then:
        response.status == HttpStatus.OK.value

        and:
        1 * em.persist({
            verifyAll(it, Livro) {
                titulo == requestData.titulo
                resumo == requestData.resumo
                sumario == requestData.sumario
                preco == requestData.preco
                paginas == requestData.paginas
                isbn == requestData.isbn
                dataPublicacao ==LocalDate.parse(requestData.dataPublicacao,  DateTimeFormatter.ISO_LOCAL_DATE)
                categoria == categoriaAssociada
                autor == autorAssociado
            }
        })
    }
}
