package com.luazevedo.lucommerce.controllers.handlers;

import com.luazevedo.lucommerce.dto.CustomError;
import com.luazevedo.lucommerce.dto.ValidationError;
import com.luazevedo.lucommerce.services.exceptions.DatabaseException;
import com.luazevedo.lucommerce.services.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomError> resourceNotFound (ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND; //= erro 404
        CustomError err = new CustomError (Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<CustomError> database (DatabaseException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; //= erro 400
        CustomError err = new CustomError (Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomError> methodArgumentNotValidation (MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;//= erro 400
        ValidationError err = new ValidationError (Instant.now(), status.value(), "Dados inválidos", request.getRequestURI());

        //for abaixo é uma lista de todos os erros previstos nas anotations do ProductDTO
        for (FieldError f: e.getBindingResult().getFieldErrors()) {
            err.addError(f.getField(), f.getDefaultMessage());
        }
        return ResponseEntity.status(status).body(err);

    }
    /*
    // Captura exceções genéricas para evitar erro 500 inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomError> genericException(Exception e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // Retorna 500
        CustomError err = new CustomError(Instant.now(), status.value(), "Erro interno no servidor", request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    */

}
//Com a anotação @ControllerAdvice, podemos definir tratamentos globais para exceções específicas, sem precisas
//ficar colocando o try-catch em várias partes do código.

