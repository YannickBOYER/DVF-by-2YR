package fr.esgi.dvf.controller;

import fr.esgi.dvf.exception.ImportNotCompletedException;
import fr.esgi.dvf.exception.MissingParamException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LigneTransactionRestControllerAdvice {
    @ExceptionHandler(MissingParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleMissingParamException(MissingParamException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ImportNotCompletedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleImportNotCompletedException(ImportNotCompletedException e) {
        return e.getMessage();
    }
}
