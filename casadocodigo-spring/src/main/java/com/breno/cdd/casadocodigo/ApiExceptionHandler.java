package com.breno.cdd.casadocodigo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
// Carga: 4
class ApiExceptionHandler extends ResponseEntityExceptionHandler {

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
        ErrorResponse errorResponse = new ErrorResponse("Erro de validação");

        // 1
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errorResponse.addValidationError(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            RuntimeException exception,
            WebRequest request
    ) {
        String errorContext = "[path: "+ request.getContextPath() + "][params: " + request.getParameterMap() + "]";
        log.error("Unknown error occurred: " + errorContext, exception);

        return buildErrorResponse(exception, "Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request)
    {
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                httpStatus);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus
    ) {
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
