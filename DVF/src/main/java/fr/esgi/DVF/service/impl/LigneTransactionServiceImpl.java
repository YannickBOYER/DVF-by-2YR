package fr.esgi.DVF.service.impl;

import fr.esgi.DVF.business.LigneTransaction;
import fr.esgi.DVF.repository.LigneTransactionRepository;
import fr.esgi.DVF.service.LigneTransactionService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPInputStream;

@Service
public class LigneTransactionServiceImpl implements LigneTransactionService {
    private LigneTransactionRepository ligneTransactionRepository;
    private final String csvCompresedFileUrlPath = "https://files.data.gouv.fr/geo-dvf/latest/csv/2023/full.csv.gz";
    private CSVParser csvParser;
    private int tailleInterval = 5000;

    private final String localFilePath = "doc/csv/full.csv";

    public LigneTransactionServiceImpl(LigneTransactionRepository ligneTransactionRepository){
        try{
            this.ligneTransactionRepository = ligneTransactionRepository;
            this.csvParser = getCSVParser();
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    public Long getNombreDeLignes(){
        return ligneTransactionRepository.count();
    }

    @Scheduled(cron = "*/15 * * * * *")
    public void importer() {
        try{
            if(csvParser.iterator().hasNext()){
                // On insère les lignes csv
                for (int i = 0; i < tailleInterval; i++){
                    if(csvParser.iterator().hasNext()){
                        CSVRecord csvRecord = csvParser.iterator().next();
                        importerLigneTransactionByCSVRecord(csvRecord);
                    }
                }
            }else{
                System.out.println("L'ensemble des lignes on été importées.");
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void importerLigneTransactionByCSVRecord(CSVRecord csvRecord) throws Exception{
        LigneTransaction ligneTransaction;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String idMutation = csvRecord.get("id_mutation");
        Date dateMutation = dateFormat.parse(csvRecord.get("date_mutation"));
        double longitude = Double.parseDouble(csvRecord.get("longitude"));
        double latitude = Double.parseDouble(csvRecord.get("latitude"));

        ligneTransaction = new LigneTransaction(idMutation, dateMutation, longitude, latitude);
        ligneTransactionRepository.save(ligneTransaction);
    }

    private CSVParser getCSVParser() throws Exception{
        downloadAndDecompressGzip();
        File file = new File(localFilePath);
        Reader reader = new InputStreamReader(new BufferedInputStream(file.toURL().openStream()));
        CSVFormat csvFormat = CSVFormat.newFormat(',').builder().setHeader().setSkipHeaderRecord(true).build();
        return new CSVParser(reader, csvFormat);
    }

    private void downloadAndDecompressGzip() throws IOException {
        URL url = new URL(csvCompresedFileUrlPath);

        try (InputStream in = url.openStream();
             GZIPInputStream gzipIn = new GZIPInputStream(in);
             FileOutputStream out = new FileOutputStream(localFilePath)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gzipIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
