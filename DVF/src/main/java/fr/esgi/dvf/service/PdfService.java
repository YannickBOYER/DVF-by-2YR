package fr.esgi.dvf.service;

import fr.esgi.dvf.business.Pdf;
import fr.esgi.dvf.dto.PdfGenerateDto;

public interface PdfService {

    public void lancerProcedureGeneration(Double longitude, Double latitude, Integer rayon);

    public void generer(PdfGenerateDto pdfGenerateDto);
}
