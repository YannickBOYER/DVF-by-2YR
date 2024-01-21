package fr.esgi.dvf.automate;

import fr.esgi.dvf.dto.PdfLocationDto;
import fr.esgi.dvf.service.PdfService;
import lombok.AllArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PdfGenerateQueue {

    private PdfService pdfService;

    @JmsListener(destination = "generatePdf")
    public void generatePdf(PdfLocationDto pdfLocationDto) {
        pdfService.generer(pdfLocationDto);
    }
}
