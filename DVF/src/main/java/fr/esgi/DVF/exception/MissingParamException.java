package fr.esgi.DVF.exception;

public class MissingParamException extends RuntimeException{
    public MissingParamException(String message){
        super(message);
    }
}
