package fr.esgi.dvf.service;

import fr.esgi.dvf.business.LigneTransaction;

import java.util.Map;

public interface LigneTransactionService{

    public Map<String, LigneTransaction> findAllByLocation(Double longitude, Double latitude, Integer rayon);

    public boolean isImportCompleted();
}
