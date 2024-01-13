package fr.esgi.DVF.view;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import fr.esgi.DVF.business.LigneTransaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.text.SimpleDateFormat;
import java.util.Map;

public class DvfPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        PdfContentByte cb = writer.getDirectContent();
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        document.newPage();
        document.addTitle("Ligne Transaction");

        int startY = 800; // Position verticale initiale
        int fontSize = 12;

        afficherText("Lignes Transaction DVF", 40, startY, bf, fontSize, cb);
        ajouterSautDeLigne(document);
        for (Map.Entry<String, Object> value:model.entrySet()) {
            String key = value.getKey();
            Object objectValue = value.getValue();

            if(objectValue instanceof LigneTransaction){
                LigneTransaction ligneTransaction = (LigneTransaction) objectValue;
                /*String displayText = ligneTransaction.toAffichagePdf();
                afficherText(displayText, 40, startY, bf, fontSize, cb);*/
                PdfPTable table = createTableFromLigneTransaction(ligneTransaction);
                document.add(table);
                ajouterSautDeLigne(document);

            }
        }
        document.close();
    }

    private void ajouterSautDeLigne(Document document)throws Exception{
        Paragraph paragraph = new Paragraph(" ");
        paragraph.setSpacingBefore(20f);
        document.add(paragraph);
    }

    private void afficherText(String nom, int x, int y, BaseFont baseFont, int fontSize, PdfContentByte cb) {
        cb.beginText();
        cb.moveText(x, y);
        cb.setFontAndSize(baseFont, fontSize);
        cb.showText(nom);
        cb.endText();
    }

    private PdfPTable createTableFromLigneTransaction(LigneTransaction ligneTransaction) throws DocumentException {
        PdfPTable table = new PdfPTable(2); // 2 colonnes, une pour les clés et une pour les valeurs
        table.setWidthPercentage(70);

        addCellToTable(table, "Id mutation", ligneTransaction.getIdMutation());
        addCellToTable(table, "Date mutation", new SimpleDateFormat("dd/MM/yyyy").format(ligneTransaction.getDateMutation()));

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
