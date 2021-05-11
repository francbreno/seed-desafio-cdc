package com.breno.cdd.casadocodigo


import java.time.LocalDate

class CadastroLivroRequestDataBuilder {
    private static final TEXT_MORE_500_CHARACTERS = "a" * 501

    private Map<String, Object> data;

    CadastroLivroRequestDataBuilder() {
        data = [
                titulo: "Neuromancer",
                resumo: "Neuromancer, de William Gibson, é um dos mais famosos romances do gênero cyberpunk, e ganhou os três principais prêmios da ficção científica: Nebula, Hugo e Philip K. Dick, após sua publicação em 1984, tendo sido publicado em 1991 no Brasil pela editora Aleph",
                sumario: "O romance conta a história de Case, um ex-hacker (cowboy, como são chamados os hackers em ***Neuromancer***) que foi impossibilitado de exercer sua \"profissão\", graças a um erro que cometeu ao tentar roubar seus patrões. Eles então envenenaram Case com uma *micotoxina*, que danificou seu sistema neural e o impossibilitou de se conectar à ***Matrix***. Antes deixaram uma quantia de dinheiro com ele, pois *\"iria precisar dele\"*.",
                preco: 20.0,
                paginas: 100,
                isbn: "8576573008",
                dataPublicacao: LocalDate.now().plusDays(1).format("yyyy-MM-dd"),
                idCategoria: 1,
                idAutor: 1
        ]
    }

    Map cria() {
        return this.data;
    }

    CadastroLivroRequestDataBuilder comTitulo(String titulo) {
        this.data.titulo = titulo
        return this
    }

    CadastroLivroRequestDataBuilder comTituloEmBranco() {
        this.data.titulo = ""
        return this;
    }

    CadastroLivroRequestDataBuilder comTituloNulo() {
        this.data.titulo = null
        return this
    }

    CadastroLivroRequestDataBuilder comResumoEmBranco() {
        this.data.resumo = ""
        return this
    }

    CadastroLivroRequestDataBuilder comResumoNulo() {
        this.data.resumo = null
        return this;
    }

    CadastroLivroRequestDataBuilder comResumoComMaisCaracteresQueLimite() {
        this.data.resumo = TEXT_MORE_500_CHARACTERS
        return this
    }

    CadastroLivroRequestDataBuilder comResumo(String resumo) {
        this.data.resumo = resumo
        return this
    }

    CadastroLivroRequestDataBuilder comSumarioEmBranco() {
        this.data.sumario = ""
        return this
    }

    CadastroLivroRequestDataBuilder comSumarioNulo() {
        this.data.sumario = null
        return this
    }

    CadastroLivroRequestDataBuilder comPrecoAbaixoMinimo() {
        this.data.preco = 19.99
        return this
    }

    CadastroLivroRequestDataBuilder comPaginasAbaixoMinimo() {
        this.data.paginas = 99
        return this;
    }

    CadastroLivroRequestDataBuilder comIsbn(String isbn) {
        this.data.isbn = isbn
        return this
    }

    CadastroLivroRequestDataBuilder comIsbnEmBranco() {
        this.data.isbn = ""
        return this
    }

    CadastroLivroRequestDataBuilder comDataPublicacaoNoPassado() {
        this.data.dataPublicacao = LocalDate.now().minusDays(1).format("yyyy-MM-dd")
        return this
    }

    CadastroLivroRequestDataBuilder comDataPublicacaoEmFormatoInvalid() {
        this.data.dataPublicacao = "22/11/2020"
        return this
    }

    CadastroLivroRequestDataBuilder comIdCategoria(Long id) {
        this.data.idCategoria = id
        return this
    }

    CadastroLivroRequestDataBuilder comIdAutor(Long id) {
        this.data.idAutor = id
        return this
    }

    def CadastroLivroRequestDataBuilder comPrecoNulo() {
        this.data.preco = null
        return this
    }

    def CadastroLivroRequestDataBuilder comPaginasNula() {
        this.data.paginas = null
        return this
    }

    def CadastroLivroRequestDataBuilder comDataPublicacaoNula() {
        this.data.dataPublicacao = null
        return this
    }

    def CadastroLivroRequestDataBuilder comIsbnNulo() {
        this.data.isbn = null
        return this
    }

    def CadastroLivroRequestDataBuilder semTitulo() {
        this.data.remove("titulo")
        return this
    }

    def CadastroLivroRequestDataBuilder semResumo() {
        this.data.remove("resumo")
        return this
    }

    def CadastroLivroRequestDataBuilder semSumario() {
        this.data.remove("sumario")
        return this
    }

    def CadastroLivroRequestDataBuilder semPreco() {
        this.data.remove("preco")
        return this
    }

    def CadastroLivroRequestDataBuilder semPaginas() {
        this.data.remove("paginas")
        return this
    }

    def CadastroLivroRequestDataBuilder semDataPublicacao() {
        this.data.remove("dataPublicacao")
        return this
    }

    def CadastroLivroRequestDataBuilder semIsbn() {
        this.data.remove("isbn")
        return this
    }
}
