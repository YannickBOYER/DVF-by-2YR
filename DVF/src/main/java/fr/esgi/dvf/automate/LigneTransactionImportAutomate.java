package fr.esgi.dvf.automate;

import fr.esgi.dvf.service.LigneTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;

@Component
@AllArgsConstructor
public class LigneTransactionImportAutomate {
    LigneTransactionService ligneTransactionService;

    @Scheduled(cron = "*/10 * * * * *")
    //@Scheduled(cron = "0 */5 * * * *")
    public void importScheduled() throws ParseException, IOException {
        ligneTransactionService.importer();
    }
}
