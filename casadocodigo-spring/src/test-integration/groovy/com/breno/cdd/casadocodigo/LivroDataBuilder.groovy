package com.breno.cdd.casadocodigo

import com.breno.cdd.casadocodigo.cadastrolivro.Livro

import java.time.LocalDate

class LivroDataBuilder {
    private Livro.LivroBuilder builder;

    public LivroDataBuilder() {
        this.builder = Livro.builder()
            .titulo("Neuromancer")
            .resumo("Neuromancer, de William Gibson, é um dos mais famosos romances do gênero cyberpunk, e ganhou os três principais prêmios da ficção científica: Nebula, Hugo e Philip K. Dick, após sua publicação em 1984, tendo sido publicado em 1991 no Brasil pela editora Aleph")
            .sumario("O romance conta a história de Case, um ex-hacker (cowboy, como são chamados os hackers em ***Neuromancer***) que foi impossibilitado de exercer sua \"profissão\", graças a um erro que cometeu ao tentar roubar seus patrões. Eles então envenenaram Case com uma *micotoxina*, que danificou seu sistema neural e o impossibilitou de se conectar à ***Matrix***. Antes deixaram uma quantia de dinheiro com ele, pois *\"iria precisar dele\"*.")
            .preco(20.0)
            .paginas(100)
            .isbn("8576573008")
            .dataPublicacao(LocalDate.now().plusDays(1))
            .categoria(new CategoriaDataBuilder().cria())
            .autor(new AutorDataBuilder().cria())
    }

    Livro cria() {
        return this.builder.build()
    }
}
