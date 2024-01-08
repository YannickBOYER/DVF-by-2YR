package fr.esgi.DVF.controller;

import fr.esgi.DVF.business.LigneTransaction;
import fr.esgi.DVF.dto.LocationDTO;
import fr.esgi.DVF.exception.MissingParamException;
import fr.esgi.DVF.service.LigneTransactionService;
import fr.esgi.DVF.view.DvfPdfView;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("LigneTransaction")
public class LigneTransactionRestController {
    private LigneTransactionService ligneTransactionService;

    @GetMapping("generatePdfByLocation")
    public ModelAndView generatePdfByLocation(@RequestBody LocationDTO locationDTO){
        if (locationDTO.longitude == null || locationDTO.latitude == null || locationDTO.rayon == null) {
            throw new MissingParamException("Les paramètres longitude, latitude et rayon sont obligatoires");
        }
        System.out.println("PDF généré avec succès pour la localisation : " + locationDTO.longitude + ", " + locationDTO.latitude);
        ModelAndView mav = new ModelAndView(new DvfPdfView(), null);
        return mav;
    }

    @GetMapping("nombreLigneImportees")
    public ResponseEntity<String> getNombreDeLignes(){
        Long nbrLignesImportees = ligneTransactionService.getNombreDeLignes();
        return ResponseEntity.ok(nbrLignesImportees + " lignes ont été importées.");
    }

    @GetMapping("generatePdfByLocationParam")
    public ModelAndView generatePdfByLocationParam(@RequestParam(name = "longitude", required = false) Double longitude, @RequestParam(name = "latitude", required = false) Double latitude, @RequestParam(name = "rayon", required = false) Integer rayon){
        if (longitude == null || latitude == null || rayon == null) {
            throw new MissingParamException("Les paramètres longitude, latitude et rayon sont obligatoires");
        }
        Map<String, LigneTransaction> lignesTransactionByLocation = ligneTransactionService.getLigneTransactionByLocation(longitude, latitude, rayon);
        System.out.println("PDF généré avec succès pour la localisation : " + longitude + ", " + latitude);
        ModelAndView mav = new ModelAndView(new DvfPdfView(), lignesTransactionByLocation);
        return mav;
    }

}

