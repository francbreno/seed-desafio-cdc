package com.breno.cdd.casadocodigo.cadastrolivro;

import com.breno.cdd.casadocodigo.cadastrocategoria.Categoria;
import com.breno.cdd.casadocodigo.cadastronovoautor.Autor;
import com.breno.cdd.casadocodigo.compartilhado.AssociatedEntityNotFoundException;
import com.breno.cdd.casadocodigo.compartilhado.UniqueValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Value
public class CadastroLivroRequest {
    @NotBlank
    @UniqueValue(domainClass = Livro.class, property = "titulo")
    private final String titulo;

    @NotBlank
    @Size(max = 500)
    private final String resumo;

    @NotBlank
    private final String sumario;

    @NotNull
    @Min(20)
    private final BigDecimal preco;

    @NotNull
    @Min(100)
    private final Integer paginas;

    @NotBlank
    @UniqueValue(domainClass = Livro.class, property = "isbn")
    private final String isbn;

    @NotNull
    @Future
    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate dataPublicacao;

    @NotNull
    private final Long idCategoria;

    @NotNull
    private final Long idAutor;

    @Builder
    public CadastroLivroRequest(
            @NotBlank String titulo,
            @NotBlank @Size(max = 500) String resumo,
            @NotBlank String sumario,
            @NotNull @Min(20) BigDecimal preco,
            @NotNull @Min(100) Integer paginas,
            @NotBlank String isbn,
            @NotNull @Future LocalDate dataPublicacao,
            @NotNull Long idCategoria,
            @NotNull Long idAutor)
    {
        Assert.hasText(titulo, "Título não pode ser nulo ou vazio");
        Assert.hasText(resumo, "Resumo não pode ser nulo ou vazio");
        Assert.hasText(sumario, "Sumário não pode ser vazio");
        Assert.hasText(isbn, "ISBN não pode ser nulo ou vazio");
        Assert.isTrue(BigDecimal.valueOf(20.0).compareTo(preco) < 1, "Preco deve ser no mínimo 20");
        Assert.isTrue(paginas > 99, "Paginas deve ser no mínimo 100");
        Assert.isTrue(LocalDate.now().isBefore(dataPublicacao), "Data de publicacao deve ser uma data futura");
        Assert.notNull(idCategoria, "Id da categoria não pode ser nulo");
        Assert.notNull(idAutor, "Id da categoria não pode ser nulo");

        this.titulo = titulo;
        this.resumo = resumo;
        this.sumario = sumario;
        this.preco = preco;
        this.paginas = paginas;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.idCategoria = idCategoria;
        this.idAutor = idAutor;
    }

    public Livro toModel(EntityManager em) {
        Categoria categoria = Optional.ofNullable(em.find(Categoria.class, this.idCategoria)).orElseThrow(() -> new AssociatedEntityNotFoundException(Categoria.class, idCategoria));
        Autor autor = Optional.ofNullable(em.find(Autor.class, this.idAutor)).orElseThrow(() -> new AssociatedEntityNotFoundException(Autor.class, idAutor));

        return new Livro.LivroBuilder()
                .titulo(this.titulo)
                .resumo(this.resumo)
                .sumario(this.sumario)
                .preco(this.preco)
                .paginas(this.paginas)
                .isbn(this.isbn)
                .dataPublicacao(this.dataPublicacao)
                .categoria(categoria)
                .autor(autor)
                .build();
    }
}
