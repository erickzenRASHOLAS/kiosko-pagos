package cl.kiosko.ms_pagos.DTO;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ExceptionDTO {
    private int code;
    private String type;
    private String date;
    private String message;
    //Funciona como plantilla de errores, muestra el codigo, el porqué, la fecha y un mensaje
    public ExceptionDTO(HttpStatus httpStatus, Exception exception) {
        this.code = httpStatus.value();
        this.type = httpStatus.getReasonPhrase();
        this.date = (new Date()).toString();
        this.message = exception.getMessage();
    }

}

