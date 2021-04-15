package com.breno.cdd.casadocodigo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
// Carga = 2
class ErrorResponse {
    private final String message;

    @Setter
    private String errorCode;

    @Setter
    private String stackTrace;
    // 1
    private List<ValidationError> errors;

    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public ErrorResponse(@NotBlank String message) {
        Objects.requireNonNull(message, "Mensagem de erro não pode ser nula");
        // 1
        if ("".equals(message)){
            throw new IllegalArgumentException("Mensagem de erro não pode ser vazia");
        }

        this.message = message;
        this.errors = new ArrayList<>();
    }

    public void addValidationError(@NotBlank String field, @NotBlank String message) {
        Objects.requireNonNull(field);
        Objects.requireNonNull(message);

        errors.add(new ValidationError(field, message));
    }
}
