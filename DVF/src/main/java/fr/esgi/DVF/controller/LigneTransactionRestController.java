package fr.esgi.DVF.controller;

import fr.esgi.DVF.dto.LocationDTO;
import fr.esgi.DVF.service.LigneTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("LigneTransaction")
public class LigneTransactionRestController {
    private LigneTransactionService ligneTransactionService;

    @GetMapping("generatePdfByLocation")
    public ResponseEntity<String> generatePdfByLocation(@RequestBody LocationDTO locationDTO){
        Double longitude = locationDTO.longitude;
        Double latitude = locationDTO.latitude;
        int rayon = locationDTO.rayon;

        return ResponseEntity.ok("PDF généré avec succès pour la localisation : " + longitude + ", " + latitude);
    }

    @GetMapping("nombreLigneImportees")
    public ResponseEntity<String> getNombreDeLignes(){
        Long nbrLignesImportees = ligneTransactionService.getNombreDeLignes();
        return ResponseEntity.ok(nbrLignesImportees + " lignes ont été importées.");
    }
    
}

