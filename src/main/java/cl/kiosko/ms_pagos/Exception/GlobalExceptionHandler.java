package cl.kiosko.ms_pagos.Exception;

import cl.kiosko.ms_pagos.DTO.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // 1. NOT FOUND (ID no econtrado)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionDTO> handleNotFound(NoSuchElementException ex) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.NOT_FOUND, ex);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.NOT_FOUND);
    }

    // 2. VALIDATION (Para los @NotNull y @Min del DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleValidation(MethodArgumentNotValidException ex) {
        // Extraemos el mensaje que tú mismo escribiste en el DTO
        String mensajeError = ex.getBindingResult().getFieldError().getDefaultMessage();
        ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.BAD_REQUEST, new Exception(mensajeError));
        return new ResponseEntity<>(exceptionDTO, HttpStatus.BAD_REQUEST);
    }

    //CONFLICT (Errores de base de datos)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ExceptionDTO> handleConflict(SQLIntegrityConstraintViolationException ex) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.CONFLICT, ex);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.CONFLICT);
    }

    //CATCH ALL
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleAllException(Exception ex) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(HttpStatus.INTERNAL_SERVER_ERROR, ex);
        return new ResponseEntity<>(exceptionDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
