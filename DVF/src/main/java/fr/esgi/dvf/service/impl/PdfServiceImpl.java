package fr.esgi.dvf.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import fr.esgi.dvf.business.LigneTransaction;
import fr.esgi.dvf.business.Pdf;
import fr.esgi.dvf.dto.PdfLocationDto;
import fr.esgi.dvf.exception.ImportNotCompletedException;
import fr.esgi.dvf.exception.MissingParamException;
import fr.esgi.dvf.exception.PdfGeneratedException;
import fr.esgi.dvf.exception.PdfNotFoundException;
import fr.esgi.dvf.repository.PdfRepository;
import fr.esgi.dvf.service.LigneTransactionService;
import fr.esgi.dvf.service.PdfService;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Map;

@Service
@AllArgsConstructor
public class PdfServiceImpl implements PdfService {
    private final Logger logger = LogManager.getLogger(PdfServiceImpl.class);
    private PdfRepository pdfRepository;
    private LigneTransactionService ligneTransactionService;
    private JmsTemplate jmsTemplate;

    private Pdf getById(Long id){
        return pdfRepository.findById(id).orElseThrow(()-> new PdfNotFoundException("Pdf non trouvé"));
    }

    @Override
    public File generateByLocation(Double longitude, Double latitude, Integer rayon) throws JMSException {
        if (longitude == null || latitude == null || rayon == null) {
            throw new MissingParamException("Les paramètres longitude, latitude et rayon sont obligatoires");
        }
        if(!ligneTransactionService.isImportCompleted()) {
            throw new ImportNotCompletedException("L'import du fichier CSV n'est pas terminé.");
        }
        Pdf pdf = pdfRepository.save(new Pdf());

        PdfLocationDto requestDto = new PdfLocationDto(longitude, latitude, rayon, pdf.getId());
        Message response = jmsTemplate.sendAndReceive("generatePdf",
                session -> session.createObjectMessage(requestDto));

        String path = "";
        if(response != null){
            path = ((TextMessage)response).getText();
        }else{
            throw new PdfGeneratedException("Une erreur est survenue lors de la génération du fichier pdf");
        }

        return new File(path);
    }

    @Override
    public String generer(PdfLocationDto pdfLocationDto) {
        Map<String, LigneTransaction> lignesTransactionByLocation = ligneTransactionService.findAllByLocation(pdfLocationDto.longitude, pdfLocationDto.latitude, pdfLocationDto.rayon);
        File pdf = this.generateFile(lignesTransactionByLocation.values(), pdfLocationDto.idPdf);
        updatePath(pdfLocationDto.idPdf, pdf.getPath());
        return pdf.getPath();
    }

    private void updatePath(Long id, String newPath){
        Pdf pdf = getById(id);
        pdf.setPath(newPath);
        pdfRepository.save(pdf);
    }

    private File generateFile(Collection<LigneTransaction> lignesTransactionByLocation, Long idPdf){
        File pdf = new File("doc\\pdf\\pdf - " + idPdf + ".pdf");
        try{
            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdf));
            document.open();

            BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

            document.addTitle("Ligne Transaction");

            int fontSize = 12;
            Font font = new Font(bf, fontSize);

            document.add(new Paragraph("Lignes Transaction DVF", font));
            ajouterSautDeLigne(document);

            for (LigneTransaction ligneTransaction : lignesTransactionByLocation) {
                PdfPTable table = createTableFromLigneTransaction(ligneTransaction);
                document.add(table);
                ajouterSautDeLigne(document);
            }

            document.close();
            pdfWriter.close();

        }catch (Exception ex){
            logger.warn(ex.getMessage());
        }
        return pdf;
    }

    private void ajouterSautDeLigne(Document document)throws DocumentException{
        Paragraph paragraph = new Paragraph(" ");
        paragraph.setSpacingBefore(20f);
        document.add(paragraph);
    }

    private PdfPTable createTableFromLigneTransaction(LigneTransaction ligneTransaction) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);
        table.setTotalWidth(new float[]{150, 350});

        addCellToTable(table, "Id mutation", ligneTransaction.getIdMutation());
        if(ligneTransaction.getDateMutation() != null)
            addCellToTable(table, "Date mutation", new SimpleDateFormat("dd/MM/yyyy").format(ligneTransaction.getDateMutation()));
        if(ligneTransaction.getNatureMutation() != null)
            addCellToTable(table, "Nature mutation", ligneTransaction.getNatureMutation());
        if(ligneTransaction.getValeurFonciere() != null)
            addCellToTable(table, "Valeur foncière", ligneTransaction.getValeurFonciere().toString());
        if(ligneTransaction.getAdresse() != null)
            addCellToTable(table, "Adresse", ligneTransaction.getAdresse());
        if(ligneTransaction.getIdParcelle() != null)
            addCellToTable(table, "Id parcelle", ligneTransaction.getIdParcelle());
        if(ligneTransaction.getNombreLots() != null)
            addCellToTable(table, "Nombre de lots", ligneTransaction.getNombreLots().toString());
        if(ligneTransaction.getTypeLocal() != null)
            addCellToTable(table, "Type local", ligneTransaction.getTypeLocal());
        if(ligneTransaction.getSurfaceReelleBati() != null)
            addCellToTable(table, "Surface réelle batiment", ligneTransaction.getSurfaceReelleBati().toString());
        if(ligneTransaction.getNombrePiecesPrincipales() != null)
            addCellToTable(table, "Nbr pièces principales", ligneTransaction.getNombrePiecesPrincipales().toString());
        if(ligneTransaction.getSurfaceTerrain() != null)
            addCellToTable(table, "Surface terrain", ligneTransaction.getSurfaceTerrain().toString());

        return table;
    }

    private void addCellToTable(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label));
        table.addCell(labelCell);
        if(value != null){
            PdfPCell valueCell = new PdfPCell(new Phrase(value));
            table.addCell(valueCell);
        }
    }
}
