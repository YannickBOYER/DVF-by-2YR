package fr.esgi.DVF.controller;

import fr.esgi.DVF.dto.LocationDTO;
import fr.esgi.DVF.service.LigneTransactionService;
import fr.esgi.DVF.view.DvfPdfView;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@AllArgsConstructor
@RequestMapping("LigneTransaction")
public class LigneTransactionRestController {
    private LigneTransactionService ligneTransactionService;

    @GetMapping("generatePdfByLocation")
    public ModelAndView generatePdfByLocation(@RequestBody LocationDTO locationDTO){
        System.out.println("PDF généré avec succès pour la localisation : " + locationDTO.longitude + ", " + locationDTO.latitude);
        ModelAndView mav = new ModelAndView(new DvfPdfView(), null);
        return mav;
    }

    @GetMapping("nombreLigneImportees")
    public ResponseEntity<String> getNombreDeLignes(){
        Long nbrLignesImportees = ligneTransactionService.getNombreDeLignes();
        return ResponseEntity.ok(nbrLignesImportees + " lignes ont été importées.");
    }
    
}

