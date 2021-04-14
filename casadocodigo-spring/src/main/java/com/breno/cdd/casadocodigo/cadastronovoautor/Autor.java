package com.breno.cdd.casadocodigo.cadastronovoautor;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE) // Para atender exigência do framework
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
// Carga: 0
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private final String nome;

    @Column(nullable = false)
    @Email
    @NotBlank
    private final String email;

    @Column(nullable = false, length = 400)
    @NotBlank
    @Size(max = 400)
    private final String descricao;

    @Column(updatable = false)
    private LocalDateTime instante;

    public Autor(@NotBlank String nome, @Email @NotBlank String email, @NotBlank @Size(max = 400) String descricao) {
        Objects.requireNonNull(nome, "Nome não pode ser nulo");
        Objects.requireNonNull(email, "Email não pode ser nulo");
        Objects.requireNonNull(descricao, "Descricao não pode ser nulo");

        this.nome = nome;
        this.email = email;
        this.descricao = descricao;
    }

    @PrePersist
    private void defineInstante() {
        this.instante = LocalDateTime.now();
    }

    void setId(Long id) {
        this.id = id;
    }
}
