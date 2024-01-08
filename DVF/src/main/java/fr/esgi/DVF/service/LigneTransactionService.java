package fr.esgi.DVF.service;

import fr.esgi.DVF.business.LigneTransaction;
import fr.esgi.DVF.dto.LocationDTO;

import java.util.Map;

public interface LigneTransactionService{

    public Long getNombreDeLignes();

    public Map<String, LigneTransaction> getLigneTransactionByLocation(LocationDTO location);

    public Map<String, LigneTransaction> getLigneTransactionByLocation(Double longitude, Double latitude, Integer rayon);
}
