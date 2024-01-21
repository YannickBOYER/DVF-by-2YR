package fr.esgi.dvf.exception;

public class PdfNotFoundException extends RuntimeException {
    public PdfNotFoundException(String message){
        super(message);
    }
}
