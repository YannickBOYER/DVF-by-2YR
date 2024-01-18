package fr.esgi.dvf.exception;

public class ImportNotCompletedException extends RuntimeException{
    public ImportNotCompletedException(String message){
        super(message);
    }
}
