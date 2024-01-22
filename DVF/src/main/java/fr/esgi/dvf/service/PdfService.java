package fr.esgi.dvf.service;

import fr.esgi.dvf.dto.PdfLocationDto;
import jakarta.jms.JMSException;

import java.io.File;

public interface PdfService {

    public File generateByLocation(Double longitude, Double latitude, Integer rayon) throws JMSException;

    public String generer(PdfLocationDto pdfGenerateDto);
}
