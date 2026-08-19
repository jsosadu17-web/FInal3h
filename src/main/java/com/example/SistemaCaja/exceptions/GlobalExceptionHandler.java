package com.example.SistemaCaja.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RecursoNoEncontradoException.class)
    ResponseEntity<ErrorResponse> notFound(RecursoNoEncontradoException ex, HttpServletRequest req) { return error(HttpStatus.NOT_FOUND, ex.getMessage(), req); }

    @ExceptionHandler({ReglaNegocioException.class, SimulacionException.class})
    ResponseEntity<ErrorResponse> business(RuntimeException ex, HttpServletRequest req) { return error(HttpStatus.BAD_REQUEST, ex.getMessage(), req); }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(e -> e.getField() + ": " + e.getDefaultMessage()).collect(Collectors.joining("; "));
        return error(HttpStatus.BAD_REQUEST, message, req);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> unexpected(Exception ex, HttpServletRequest req) { return error(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado", req); }

    private ResponseEntity<ErrorResponse> error(HttpStatus status, String message, HttpServletRequest req) {
        return ResponseEntity.status(status).body(ErrorResponse.builder().timestamp(LocalDateTime.now()).status(status.value())
                .error(status.getReasonPhrase()).message(message).path(req.getRequestURI()).build());
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ErrorResponse> manejarCredencialesInvalidas(CredencialesInvalidasException ex,
                                                                      HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
