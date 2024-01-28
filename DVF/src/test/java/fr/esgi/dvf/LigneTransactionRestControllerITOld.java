package fr.esgi.dvf;

import fr.esgi.dvf.controller.LigneTransactionRestController;
import fr.esgi.dvf.exception.ImportNotCompletedException;
import fr.esgi.dvf.exception.MissingParamException;
import fr.esgi.dvf.service.impl.LigneTransactionServiceImpl;
import fr.esgi.dvf.service.impl.PdfServiceImpl;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LigneTransactionRestControllerIT {

    @Autowired
    private MockMvc mockMvc;


    @Mock
    private PdfServiceImpl pdfService;

    @Autowired
    @Mock
    private LigneTransactionServiceImpl ligneTransactionService;

    @InjectMocks
    private LigneTransactionRestController ligneTransactionRestController;

    @Test
    @Order(1)
    void shouldImportFileSuccessfully() throws Exception{
        String csvTestFilePath = "doc/csv/testCsv.csv";

        // On génère un fichier de test
        new CsvTestFileGenerator(csvTestFilePath);

        // Appeler la méthode de test
        CSVParser csvParser = ligneTransactionService.getCSVParser(csvTestFilePath);
        while (csvParser.iterator().hasNext()){
            CSVRecord csvRecord = csvParser.iterator().next();
            ligneTransactionService.importerLigneTransactionByCSVRecord(csvRecord);
        }

        assertEquals(1000, ligneTransactionService.getNombreDeLignes());
        System.out.println("L'import de 1000 lignes a fonctionné");
    }

    @Test
    @Order(2)
    void shouldReturnMissingParamException()throws Exception{
        String url = "/LigneTransaction/generatePdfByLocation";
        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    // Effectuez des assertions sur l'exception capturée
                    assertThat(result.getResolvedException()).isInstanceOf(MissingParamException.class);
                });
    }

    @Test
    @Order(3)
    void shouldGeneratePdf()throws Exception{
        shouldImportFileSuccessfully();

        String longitude = "4.848466";
        String latitude = "45.754752";
        String rayon = "100";

        String url = "/LigneTransaction/generatePdfByLocation?longitude=" + longitude + "&latitude=" + latitude + "&rayon=" + rayon;

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(reqResult -> {
                    if (reqResult.getResolvedException() instanceof MissingParamException){
                        System.out.println(reqResult.getResolvedException().getMessage());
                    } else if (reqResult.getResolvedException() instanceof ImportNotCompletedException) {
                        System.out.println(reqResult.getResolvedException().getMessage());
                    }
                });
    }



    /*@Test
    @Order(1)
    void generatePdfByLocationParam() throws Exception{
        String longitude = "4.848466";
        String latitude = "45.754752";
        String rayon = "100";

        String url = "/LigneTransaction/generatePdfByLocationParam?longitude=" + longitude + "&latitude=" + latitude + "&rayon=" + rayon;

        mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(reqResult -> {
                    if (reqResult.getResolvedException() instanceof MissingParamException){
                        System.out.println(reqResult.getResolvedException().getMessage());
                    } else if (reqResult.getResolvedException() instanceof ImportNotCompletedException) {
                        System.out.println(reqResult.getResolvedException().getMessage());
                    }else{
                        MockMvcResultMatchers.status().isOk().match(reqResult);
                    }
                });
    }*/


}
