package fr.esgi.dvf.controller;

import fr.esgi.dvf.business.LigneTransaction;
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
    public ResponseEntity<Long> generatePdfByLocation(@RequestParam(name = "longitude", required = false) Double longitude, @RequestParam(name = "latitude", required = false) Double latitude, @RequestParam(name = "rayon", required = false) Integer rayon){
        Long idPdf = pdfService.lancerProcedureGeneration(longitude, latitude, rayon);
        return ResponseEntity.ok(idPdf);
    }

    @GetMapping("getPdfById")
    public ResponseEntity<byte[]> getById(@RequestParam(name = "id", required = false) Long id)throws IOException {
        Pdf pdf = pdfService.getById(id);
        File fichierPdf = new File(pdf.getPath());
        if (!fichierPdf.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        byte[] contenuPdf = Files.readAllBytes(fichierPdf.toPath());

        HttpHeaders entetes = new HttpHeaders();
        entetes.setContentType(MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(contenuPdf, entetes, HttpStatus.OK);
    }

}

