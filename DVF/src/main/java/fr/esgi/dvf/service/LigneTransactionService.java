package fr.esgi.dvf.service;

import fr.esgi.dvf.business.LigneTransaction;
import fr.esgi.dvf.dto.LocationDTO;

import java.util.Map;

public interface LigneTransactionService{

    public String getEtatImport();

    public Map<String, LigneTransaction> getLigneTransactionByLocation(LocationDTO location);

    public Map<String, LigneTransaction> getLigneTransactionByLocation(Double longitude, Double latitude, Integer rayon);
}
