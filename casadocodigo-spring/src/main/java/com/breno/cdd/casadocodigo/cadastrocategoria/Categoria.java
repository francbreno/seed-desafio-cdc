package com.breno.cdd.casadocodigo.cadastrocategoria;

import lombok.RequiredArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
class Categoria {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotBlank
   @Column(unique = true)
   private final String nome;

   public Categoria(@NotBlank String nome) {
      Assert.notNull(nome, "Nome da categoria não pode ser nulo");

      this.nome = nome;
   }
}
