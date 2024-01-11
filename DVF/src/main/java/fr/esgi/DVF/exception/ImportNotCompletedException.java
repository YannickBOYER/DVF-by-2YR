package fr.esgi.DVF.exception;

public class ImportNotCompletedException extends RuntimeException{
    public ImportNotCompletedException(String message){
        super(message);
    }
}
