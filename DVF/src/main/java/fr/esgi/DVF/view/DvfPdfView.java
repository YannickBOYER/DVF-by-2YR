package fr.esgi.DVF.view;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import java.util.Map;

public class DvfPdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        PdfContentByte cb = writer.getDirectContent();
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        document.newPage();
        afficherText("DVF : ", 40, 800, bf, 12, cb);
        // TODO : récupérer dans la map model toutes les transactions
        // effectuées dans la zone demandée
        document.close();
    }

    private void afficherText(String nom, int x, int y, BaseFont baseFont, int fontSize, PdfContentByte cb) {
        cb.beginText();
        cb.moveText(x, y);
        cb.setFontAndSize(baseFont, fontSize);
        cb.showText(nom);
        cb.endText();
    }

}
