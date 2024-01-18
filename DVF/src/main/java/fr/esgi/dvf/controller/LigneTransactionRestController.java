package fr.esgi.dvf.controller;

import fr.esgi.dvf.business.LigneTransaction;
import fr.esgi.dvf.exception.MissingParamException;
import fr.esgi.dvf.service.LigneTransactionService;
import fr.esgi.dvf.view.DvfPdfView;
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

    @GetMapping("EtatImport")
    public ResponseEntity<String> getEtatImport(){
        return ResponseEntity.ok(ligneTransactionService.getEtatImport());
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

