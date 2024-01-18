package fr.esgi.dvf.exception;

public class MissingParamException extends RuntimeException{
    public MissingParamException(String message){
        super(message);
    }
}
