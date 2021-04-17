package com.breno.cdd.casadocodigo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
// Carga = 5
class ErrorResponse {
    private final String message;

    @Setter
    private String errorCode;

    @Setter
    private String stackTrace;
    // 1
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidationError> errors;

    @Getter
    @RequiredArgsConstructor
    @EqualsAndHashCode
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public ErrorResponse(@NotBlank String message) {
        Assert.hasText(message, "Mensagem de erro não pode ser nula ou vazia");

        this.message = message;
        this.errors = new ArrayList<>();
    }

    public void addValidationError(@NotBlank String field, @NotBlank String message) {
        Assert.hasText(field, "field não pode ser nulo ou vazio");
        Assert.hasText(message, "message não pode ser nula ou vazia");

        this.errors.add(new ValidationError(field, message));
    }

    public void addFieldErrors(@Nullable List <FieldError> errors) {
        // 2
        Optional.ofNullable(errors).ifPresent(existingErrors ->
                errors.forEach(error -> addValidationError(error.getField(), error.getDefaultMessage())));
    }

    public void addGlobalErrors(@Nullable List<ObjectError> errors) {
        // 2
        Optional.ofNullable(errors).ifPresent(existingErrors ->
                errors.forEach(error -> addValidationError(error.getObjectName(), error.getDefaultMessage())));
    }
}
