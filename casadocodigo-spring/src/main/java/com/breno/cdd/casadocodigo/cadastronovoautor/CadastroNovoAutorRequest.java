package com.breno.cdd.casadocodigo.cadastronovoautor;

import lombok.NoArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Value
// Carga: 0
class CadastroNovoAutorRequest {
    @NotBlank
    private final String nome;

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    @Size(max = 400)
    private final String descricao;

    public CadastroNovoAutorRequest(
            @NotBlank String nome,
            @Email @NotBlank String email,
            @NotBlank @Size(max = 400) String descricao) {

        Objects.requireNonNull(nome, "Nome não pode ser nulo");
        Objects.requireNonNull(email, "Email não pode ser nulo");
        Objects.requireNonNull(descricao, "Descricao não pode ser nulo");

        this.nome = nome;
        this.email = email;
        this.descricao = descricao;
    }
}
