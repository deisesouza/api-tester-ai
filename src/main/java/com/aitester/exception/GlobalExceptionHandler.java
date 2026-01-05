package com.aitester.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura erros de timeout ou conexão com os microserviços
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorDetails> handleConnectivityError(ResourceAccessException ex) {
        ErrorDetails error = new ErrorDetails(
                "CONNECTION_ERROR",
                "Não foi possível conectar ao microserviço alvo: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_GATEWAY);
    }

    // Captura erros retornados pela Azure OpenAI (Ex: Chave inválida ou Cota excedida)
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorDetails> handleExternalApiError(HttpClientErrorException ex) {
        String message = "Erro na integração externa (Azure OpenAI ou Microserviço)";
        if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            message = "Falha de autenticação na Azure OpenAI. Verifique o arquivo .env";
        }

        ErrorDetails error = new ErrorDetails(
                "EXTERNAL_API_ERROR",
                message + ": " + ex.getResponseBodyAsString(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, ex.getStatusCode());
    }

    // Captura erros genéricos (Fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalError(Exception ex) {
        ErrorDetails error = new ErrorDetails(
                "INTERNAL_SERVER_ERROR",
                "Ocorreu um erro inesperado: " + ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

// DTO para padronizar o erro
record ErrorDetails(String code, String message, LocalDateTime timestamp) {}
