package fr.esgi.DVF.controller;

import fr.esgi.DVF.exception.MissingParamException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LigneTransactionRestControllerAdvice {
    @ExceptionHandler(MissingParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleParametreManquantException(MissingParamException e) {
        return e.getMessage();
    }
}
