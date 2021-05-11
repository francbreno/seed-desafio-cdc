package com.breno.cdd.casadocodigo;

import com.breno.cdd.casadocodigo.compartilhado.AssociatedEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
// Carga: 6
class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    public static final String UNEXPECTED_ERROR_MESSAGE = "Ocorreu um erro inesperado";
    public static final String VALIDATION_ERROR_MESSAGE = "Erro de validação";
    public static final String INVALID_DATA_ERROR_MESSAGE = "Não foi possível interpretar os dados fornecidos";

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request)
    {
        // 1
        ErrorResponse errorResponse = new ErrorResponse(VALIDATION_ERROR_MESSAGE);
        errorResponse.addFieldErrors(exception.getBindingResult().getFieldErrors());
        errorResponse.addGlobalErrors(exception.getBindingResult().getGlobalErrors());

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @Override
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request)
    {

        return buildErrorResponse(exception, INVALID_DATA_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AssociatedEntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleAssociatedEntityNotFound(
            AssociatedEntityNotFoundException exception)
    {
        return buildErrorResponse(exception, exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleValidationException(
            ValidationException exception)
    {
        return buildErrorResponse(exception, exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            RuntimeException exception,
            WebRequest request
    ) {
        log.error("Unknown error occurred: " + request, exception);
        return buildErrorResponse(exception, UNEXPECTED_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request)
    {
        log.error("Unknown error occurred: " + request, ex);
        return buildErrorResponse(ex,status);
    }

    private ResponseEntity<Object> buildErrorResponse(
            @NotNull Exception exception,
            HttpStatus httpStatus
    ) {
        Assert.notNull(exception, "exception não pode ser nula");

        return buildErrorResponse(
                exception,
                exception.getMessage(),
                httpStatus);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            @Nullable String message,
            @Nullable HttpStatus httpStatus
    ) {
        Assert.notNull(exception, "exception não pode ser nula");

        // 1
        HttpStatus returnedStatusCode = httpStatus == null ? HttpStatus.INTERNAL_SERVER_ERROR : httpStatus;
        // 1
        String returnedMessage = StringUtils.isBlank(message) ? UNEXPECTED_ERROR_MESSAGE : message;

        ErrorResponse errorResponse = new ErrorResponse(message);

        // 1
        if(!isRunningInProduction()) {
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private boolean isRunningInProduction() {
        // 1
        return Arrays.stream(activeProfile.split(",")).anyMatch(env -> "prod".equals(env));
    }
}
