package fr.esgi.dvf.service;

import fr.esgi.dvf.business.Pdf;
import fr.esgi.dvf.dto.PdfLocationDto;

public interface PdfService {

    public Long lancerProcedureGeneration(Double longitude, Double latitude, Integer rayon);

    public void generer(PdfLocationDto pdfGenerateDto);

    public Pdf getById(Long id);
}
