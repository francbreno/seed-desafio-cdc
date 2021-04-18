package com.breno.cdd.casadocodigo.cadastrolivro;

import com.breno.cdd.casadocodigo.cadastrocategoria.Categoria;
import com.breno.cdd.casadocodigo.cadastronovoautor.Autor;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true) // Para atender exigência do framework
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    @Column(unique = true)
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
    private final String isbn;

    @NotNull
    @Future
    private final LocalDate dataPublicacao;

    @ManyToOne
    @NotNull
    @Valid
    private final Categoria categoria;

    @ManyToOne
    @NotNull
    @Valid
    private final Autor autor;

    @Builder
    private Livro(
            @NotBlank String titulo,
            @NotBlank @Size(max = 500) String resumo,
            @NotBlank String sumario,
            @NotNull @Min(20) BigDecimal preco,
            @NotNull @Min(100) Integer paginas,
            @NotBlank String isbn,
            @NotNull @Future LocalDate dataPublicacao,
            @NotNull @Valid Categoria categoria,
            @NotNull @Valid Autor autor)
    {
        Assert.hasText(titulo, "Título não pode ser nulo ou vazio");
        Assert.hasText(resumo, "Resumo não pode ser nulo ou vazio");
        Assert.hasText(sumario, "Sumário não pode ser vazio");
        Assert.hasText(isbn, "ISBN não pode ser nulo ou vazio");
        Assert.isTrue(BigDecimal.valueOf(20.0).compareTo(preco) < 1, "Preco deve ser no mínimo 20");
        Assert.isTrue(paginas > 99, "Paginas deve ser no mínimo 100");
        Assert.isTrue(LocalDate.now().isBefore(dataPublicacao), "Data de publicacao deve ser uma data futura");
        Assert.notNull(categoria, "Categoria não pode ser nula");
        Assert.notNull(autor, "Autor não pode ser nulo");

        this.titulo = titulo;
        this.resumo = resumo;
        this.sumario = sumario;
        this.preco = preco;
        this.paginas = paginas;
        this.isbn = isbn;
        this.dataPublicacao = dataPublicacao;
        this.categoria = categoria;
        this.autor = autor;
    }
}
