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
import java.util.Properties;
import java.util.zip.GZIPInputStream;

@Service
public class LigneTransactionServiceImpl implements LigneTransactionService {
    private LigneTransactionRepository ligneTransactionRepository;
    private final Properties importProperties = new Properties();
    private CSVParser csvParser;
    private int nbrLigneIgnoree;
    private boolean isImportTermine = false;

    public LigneTransactionServiceImpl(LigneTransactionRepository ligneTransactionRepository){
        try{
            this.ligneTransactionRepository = ligneTransactionRepository;
            this.importProperties.load(new FileInputStream("src/main/resources/import.properties"));
            this.csvParser = getCSVParser();
        }catch (Exception exception){
            System.out.println(exception.getMessage());
        }
    }

    public Long getNombreDeLignes(){
        return ligneTransactionRepository.count();
    }

    @Scheduled(cron = "*/30 * * * * *")
    //@Scheduled(cron = "0 */5 * * * *")
    public void importer() {
        try{
            if(!isImportTermine){
                if(csvParser.iterator().hasNext()){
                    System.out.println("Importation de 100 000 lignes en base H2...");
                    for (int i = 0; i < Integer.parseInt(importProperties.getProperty("nbrIntervalLigneParImport")); i++){
                        if(csvParser.iterator().hasNext()){
                            CSVRecord csvRecord = csvParser.iterator().next();
                            importerLigneTransactionByCSVRecord(csvRecord);
                        }
                    }
                }else{
                    resultatImport();
                    isImportTermine = true;
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }

    private void resultatImport(){
        System.out.println("L'ensemble des lignes ont été importées.");
        System.out.println("Lignes importées : " + this.getNombreDeLignes() + ".");
        System.out.println("Lignes ignorées : " + this.nbrLigneIgnoree + ".");
    }

    private void importerLigneTransactionByCSVRecord(CSVRecord csvRecord) throws Exception{
        LigneTransaction ligneTransaction;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String idMutation = csvRecord.get("id_mutation");
        Date dateMutation = dateFormat.parse(csvRecord.get("date_mutation"));

        // Si les lignes n'ont pas de longitude/latitude on ne les prends pas (sert a rien)
        if(!csvRecord.get("longitude").isEmpty() && !csvRecord.get("latitude").isEmpty()){
            double longitude = Double.parseDouble(csvRecord.get("longitude"));
            double latitude = Double.parseDouble(csvRecord.get("latitude"));

            ligneTransaction = new LigneTransaction(idMutation, dateMutation, longitude, latitude);
            ligneTransactionRepository.save(ligneTransaction);
        }else{
            nbrLigneIgnoree++;
        }
    }

    private CSVParser getCSVParser() throws Exception{
        downloadAndDecompressGzip();
        File file = new File(importProperties.getProperty("localFilePath"));
        Reader reader = new InputStreamReader(new BufferedInputStream(file.toURL().openStream()));
        CSVFormat csvFormat = CSVFormat.newFormat(',').builder().setHeader().setSkipHeaderRecord(true).build();
        return new CSVParser(reader, csvFormat);
    }

    private void downloadAndDecompressGzip() throws IOException {
        URL url = new URL(importProperties.getProperty("csvCompresedFileUrlPath"));

        try (InputStream in = url.openStream();
             GZIPInputStream gzipIn = new GZIPInputStream(in);
             FileOutputStream out = new FileOutputStream(importProperties.getProperty("localFilePath"))) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = gzipIn.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }
}
