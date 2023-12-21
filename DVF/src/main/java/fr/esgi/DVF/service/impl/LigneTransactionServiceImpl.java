package fr.esgi.DVF.service.impl;

import fr.esgi.DVF.service.LigneTransactionService;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@AllArgsConstructor
public class LigneTransactionServiceImpl implements LigneTransactionService {

    private final String csvDataUrlPath = "http://www.data.gouv.fr/fr/datasets/r/dbe8a621-a9c4-4bc3-9cae-be1699c5ff25";

    @Override
    public void importer() {

        try{

            CSVFormat csvFormat = CSVFormat.newFormat(',').builder().setHeader().setSkipHeaderRecord(true).build();
            CSVRecord csvRecord = null;
            URL url = new URL(csvDataUrlPath);

            InputStreamReader reader = new InputStreamReader(new BufferedInputStream(url.openStream()));

            CSVParser csvParser = new CSVParser(reader, csvFormat);
            while(csvParser.iterator().hasNext()){
                csvRecord = csvParser.iterator().next();
            }
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }
}
