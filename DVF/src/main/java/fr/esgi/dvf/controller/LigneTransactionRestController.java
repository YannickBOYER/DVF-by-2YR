package fr.esgi.dvf.controller;

import fr.esgi.dvf.business.Pdf;
import fr.esgi.dvf.exception.MissingParamException;
import fr.esgi.dvf.service.LigneTransactionService;
import fr.esgi.dvf.service.PdfService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@AllArgsConstructor
@RequestMapping("LigneTransaction")
public class LigneTransactionRestController {
    private LigneTransactionService ligneTransactionService;
    private PdfService pdfService;

    @GetMapping("EtatImport")
    public ResponseEntity<String> getEtatImport(){
        return ResponseEntity.ok(ligneTransactionService.getEtatImport());
    }

    /*@GetMapping("generatePdfByLocation")
    public ModelAndView generatePdfByLocation(@RequestParam(name = "longitude", required = false) Double longitude, @RequestParam(name = "latitude", required = false) Double latitude, @RequestParam(name = "rayon", required = false) Integer rayon){
        if (longitude == null || latitude == null || rayon == null) {
            throw new MissingParamException("Les paramètres longitude, latitude et rayon sont obligatoires");
        }

        Map<String, LigneTransaction> lignesTransactionByLocation = ligneTransactionService.getLigneTransactionByLocation(longitude, latitude, rayon);
        System.out.println("PDF généré avec succès pour la localisation : " + longitude + ", " + latitude);
        return new ModelAndView(new DvfPdfView(), lignesTransactionByLocation);
    }*/

    @GetMapping("generatePdfByLocation")
    public ResponseEntity<byte[]> generateByLocation(@RequestParam(name = "longitude", required = false) Double longitude, @RequestParam(name = "latitude", required = false) Double latitude, @RequestParam(name = "rayon", required = false) Integer rayon) throws Exception {
        File pdf = pdfService.generateByLocation(longitude, latitude, rayon);
        if(!pdf.exists()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            byte[] contenuPdf = Files.readAllBytes(pdf.toPath());

            HttpHeaders entetes = new HttpHeaders();
            entetes.setContentType(MediaType.APPLICATION_PDF);

            return new ResponseEntity<>(contenuPdf, entetes, HttpStatus.OK);
        }
    }

}
