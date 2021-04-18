package com.breno.cdd.casadocodigo.cadastrocategoria;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@NoArgsConstructor(force = true) // Para atender exigência do framework
public
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

   void setId(Long id) {
      if (this.id != null) {
         throw new IllegalStateException("Não é permitido alterar o id de uma cateogoria que já tem id definido");
      }
      this.id = id;
   }
}
