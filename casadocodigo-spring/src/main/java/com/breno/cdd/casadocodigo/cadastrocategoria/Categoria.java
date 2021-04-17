package com.breno.cdd.casadocodigo.cadastrocategoria;

import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Entity
class Categoria {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @NotBlank
   @Column(unique = true)
   private final String nome;
}
