package fr.esgi.DVF.controller;

import fr.esgi.DVF.service.LigneTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("LigneTransaction")
public class LigneTransactionRestController {
    private LigneTransactionService ligneTransactionService;

    @GetMapping("import")
    public void importer(){
        ligneTransactionService.importer();
    }
}
