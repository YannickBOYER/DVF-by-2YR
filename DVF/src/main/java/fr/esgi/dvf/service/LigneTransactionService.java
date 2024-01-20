package fr.esgi.dvf.service;

import fr.esgi.dvf.business.LigneTransaction;
import fr.esgi.dvf.dto.PdfGenerateDto;

import java.util.Map;

public interface LigneTransactionService{

    public String getEtatImport();

    public Map<String, LigneTransaction> getLigneTransactionByLocation(Double longitude, Double latitude, Integer rayon);

    public Map<String, LigneTransaction> getLigneTransactionByLocation(PdfGenerateDto pdfGenerateDto);
}
