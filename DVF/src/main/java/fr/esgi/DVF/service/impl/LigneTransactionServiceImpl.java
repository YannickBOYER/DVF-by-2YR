package fr.esgi.DVF.service.impl;

import fr.esgi.DVF.service.LigneTransactionService;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;

@Service
@AllArgsConstructor
public class LigneTransactionServiceImpl implements LigneTransactionService {
    private final String csvDataUrlPath = "http://www.data.gouv.fr/fr/datasets/r/316795eb-a3fa-465d-b058-38ef8579da11";
    private final CSVFormat csvFormat = CSVFormat.newFormat(',').builder().setHeader().setSkipHeaderRecord(true).build();

    @Override
    public void importer() {
        try{
            CSVParser csvParser = getCSVParser();
            int lineNumber = 0;
            while (csvParser.iterator().hasNext()){
                lineNumber++;
                CSVRecord csvRecord = csvParser.iterator().next();
                System.out.println(csvRecord);

                // On prend les 15 premère ligne pour le moments
                if(lineNumber == 15){
                    break;
                }
            }
            csvParser.close();
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }

    }

    private CSVParser getCSVParser()throws Exception{
        CSVParser csvParser = null;
        Reader reader = null;

        // Tentative de récupération par URL
        URL url = new URL(csvDataUrlPath);
        reader = new InputStreamReader(new BufferedInputStream(url.openStream()));

        csvParser = new CSVParser(reader, csvFormat);
        // Si la liste d'enregistrement du csvParser est vide,
        // on tente de faire une extraction par fichier
        if(!csvParser.getRecords().isEmpty()){
            return csvParser;
        }else{
            File file = new File("C:\\Users\\robin\\Documents\\ESGI\\archi\\dvf\\file\\full.csv");
            reader = new InputStreamReader(new BufferedInputStream(file.toURL().openStream()));

            return new CSVParser(reader, csvFormat);
        }
    }
}
