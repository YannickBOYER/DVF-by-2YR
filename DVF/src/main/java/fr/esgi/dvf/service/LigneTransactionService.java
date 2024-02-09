package fr.esgi.dvf.service;

import fr.esgi.dvf.business.LigneTransaction;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

public interface LigneTransactionService{

    public void importer() throws ParseException, IOException;

    public Map<String, LigneTransaction> findAllByLocation(Double longitude, Double latitude, Integer rayon);

    public boolean isImportCompleted();
}
