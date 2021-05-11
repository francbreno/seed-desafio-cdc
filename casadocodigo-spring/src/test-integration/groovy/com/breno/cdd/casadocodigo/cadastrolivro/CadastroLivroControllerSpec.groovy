package com.breno.cdd.casadocodigo.cadastrolivro

import com.breno.cdd.casadocodigo.AutorDataBuilder
import com.breno.cdd.casadocodigo.CadastroLivroRequestDataBuilder
import com.breno.cdd.casadocodigo.CategoriaDataBuilder
import com.breno.cdd.casadocodigo.LivroDataBuilder
import com.breno.cdd.casadocodigo.MockResponseReader
import com.breno.cdd.casadocodigo.cadastrocategoria.Categoria
import com.breno.cdd.casadocodigo.cadastronovoautor.Autor
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static groovy.json.JsonOutput.toJson
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

@WebMvcTest([CadastroLivroController])
class CadastroLivroControllerSpec extends Specification {
    private static final String ALREADY_INSERTED_BOOK_TITLE = "1984"
    private static final String ALREADY_INSERTED_BOOK_ISBN = "123456789"

    @Autowired
    MockMvc mvc

    @Autowired
    ObjectMapper objectMapper

    @SpringBean
    EntityManager em = Mock();

    @SpringBean
    JdbcTemplate jdbcTemplate = Mock()

    MockResponseReader mockResponseReader

    def setup() {
        mockResponseReader = new MockResponseReader(objectMapper)
    }

    def "Deve cadastrar um livro"() {
        given:
        def requestData = new CadastroLivroRequestDataBuilder().cria()

        and:
        def categoriaExistente = new CategoriaDataBuilder().cria()
        def autorExistente = new AutorDataBuilder().cria()

        and:
        em.find(Categoria, 1L) >> categoriaExistente
        em.find(Autor, 1L) >> autorExistente
        jdbcTemplate.queryForList(_ as String, Integer, requestData.titulo) >> []
        jdbcTemplate.queryForList(_ as String, Integer, requestData.isbn) >> []

        when:
        def response = performPostToLivros(requestData)

        then:
        with(response) {
            status == HttpStatus.OK.value
            contentLength == 0
        }

        and:
        1 * em.persist({
            verifyAll(it, Livro) {
                titulo == requestData.titulo
                resumo == requestData.resumo
                sumario == requestData.sumario
                preco == requestData.preco
                paginas == requestData.paginas
                isbn == requestData.isbn
                dataPublicacao == LocalDate.parse(requestData.dataPublicacao,  DateTimeFormatter.ISO_LOCAL_DATE)
                categoria == categoriaExistente
                autor == autorExistente
            }
        })
    }

    @Unroll
    def "Não deve cadatrar um livro quando #missing associado(a) não existe"() {
        given:
        def requestData = new CadastroLivroRequestDataBuilder()
                .comIdCategoria(idCategoria)
                .comIdAutor(idAutor)
                .cria()

        and:
        em.find(Categoria, !1L) >> null
        em.find(Categoria, 1L) >> new CategoriaDataBuilder().cria()
        em.find(Autor, !1L) >> null
        em.find(Autor, 1L) >> new AutorDataBuilder().cria()

        and:
        jdbcTemplate.queryForList(_ as String, Integer, requestData.titulo) >> []
        jdbcTemplate.queryForList(_ as String, Integer, requestData.isbn) >> []

        when:
        def response = performPostToLivros(requestData)

        then:
        response.status == HttpStatus.BAD_REQUEST.value
        with(mockResponseReader.read(response)) {
            message == errorMessage
        }

        where:
        idCategoria     | idAutor   | missing       | errorMessage
        99L             | 1L        | "Autor"       | "Não foi encontrado(a) Categoria com id ${idCategoria}"
        1L              | 99L       | "Categoria"   | "Não foi encontrado(a) Autor com id ${idAutor}"
    }

    @Unroll
    def "Não deve cadastrar um livro quando #error"() {
        given:
        em.find(Categoria, !1L) >> null
        em.find(Categoria, 1L) >> new CategoriaDataBuilder().cria()
        em.find(Autor, !1L) >> null
        em.find(Autor, 1L) >> new AutorDataBuilder().cria()

        and:
        jdbcTemplate.queryForList({it.contains("titulo")}, Integer, !ALREADY_INSERTED_BOOK_TITLE) >> [new LivroDataBuilder().cria()]
        jdbcTemplate.queryForList({it.contains("titulo")}, ALREADY_INSERTED_BOOK_TITLE) >> []
        jdbcTemplate.queryForList({it.contains("isbn")}, !ALREADY_INSERTED_BOOK_ISBN) >> [new LivroDataBuilder().cria()]
        jdbcTemplate.queryForList({it.contains("isbn")}, ALREADY_INSERTED_BOOK_ISBN) >> []

        when:
        def response = performPostToLivros(requestData)

        then:
        response.status == HttpStatus.BAD_REQUEST.value()

        where:
        requestData                                                                         | error
        new CadastroLivroRequestDataBuilder().comTituloEmBranco().cria()                    | "titulo em branco"
        new CadastroLivroRequestDataBuilder().comTituloNulo().cria()                        | "titulo nulo"
        new CadastroLivroRequestDataBuilder().comTitulo(ALREADY_INSERTED_BOOK_TITLE).cria() | "titulo já cadastrado"
        new CadastroLivroRequestDataBuilder().comResumoEmBranco().cria()                    | "resumo em branco"
        new CadastroLivroRequestDataBuilder().comResumoNulo().cria()                        | "resumo nulo"
        new CadastroLivroRequestDataBuilder().comResumoComMaisCaracteresQueLimite().cria()  | "resumo muito grande"
        new CadastroLivroRequestDataBuilder().comSumarioEmBranco().cria()                   | "sumário em branco"
        new CadastroLivroRequestDataBuilder().comSumarioNulo().cria()                       | "sumário nulo"
        new CadastroLivroRequestDataBuilder().comPrecoAbaixoMinimo().cria()                 | "preco abaixo do mínimo"
        new CadastroLivroRequestDataBuilder().comPrecoNulo().cria()                         | "preco nulo"
        new CadastroLivroRequestDataBuilder().comPaginasAbaixoMinimo().cria()               | "páginas menor que mínimo"
        new CadastroLivroRequestDataBuilder().comPaginasNula().cria()                       | "páginas nula"
        new CadastroLivroRequestDataBuilder().comDataPublicacaoNoPassado().cria()           | "data publicaćão está no passado"
        new CadastroLivroRequestDataBuilder().comDataPublicacaoEmFormatoInvalid().cria()    | "data publicaćão tem formato invalido"
        new CadastroLivroRequestDataBuilder().comDataPublicacaoNula().cria()                | "data publicaćão nula"
        new CadastroLivroRequestDataBuilder().comIsbnEmBranco().cria()                      | "isbn está em branco"
        new CadastroLivroRequestDataBuilder().comIsbnNulo().cria()                          | "Isbn nulo"
        new CadastroLivroRequestDataBuilder().comIsbn(ALREADY_INSERTED_BOOK_ISBN).cria()    | "isbn já cadastrado"
    }

    @Unroll
    def "Não deve criar um novo livro sem fornecer o campo obrigatório #errorField"() {
        when:
        def response = performPostToLivros(requestData)

        then:
        response.status == HttpStatus.BAD_REQUEST.value

        and:
        with (mockResponseReader.read(response)) {
            message == com.breno.cdd.casadocodigo.ApiExceptionHandler.INVALID_DATA_ERROR_MESSAGE
        }

        and :
        0 * em.persist(_ as Livro)

        where:
        requestData << [
                new CadastroLivroRequestDataBuilder().semTitulo().cria(),
                new CadastroLivroRequestDataBuilder().semResumo().cria(),
                new CadastroLivroRequestDataBuilder().semSumario().cria(),
                new CadastroLivroRequestDataBuilder().semPaginas().cria(),
                new CadastroLivroRequestDataBuilder().semPreco().cria(),
                new CadastroLivroRequestDataBuilder().semDataPublicacao().cria(),
                new CadastroLivroRequestDataBuilder().semIsbn().cria(),
        ]
        errorField << ["titulo", "resumo", "sumário", "páginas", "preco", "data de publicacao", "isbn"]
    }

    private MockHttpServletResponse performPostToLivros(Map requestData) {
        mvc.perform(post("/livros")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(toJson(requestData)))
                .andReturn().response
    }
}
