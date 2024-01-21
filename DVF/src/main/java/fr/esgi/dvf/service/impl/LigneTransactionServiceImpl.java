package fr.esgi.dvf.service.impl;

import fr.esgi.dvf.business.LigneTransaction;
import fr.esgi.dvf.dto.PdfLocationDto;
import fr.esgi.dvf.exception.ImportNotCompletedException;
import fr.esgi.dvf.repository.LigneTransactionRepository;
import fr.esgi.dvf.service.LigneTransactionService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.Map;

@Service
public class LigneTransactionServiceImpl implements LigneTransactionService {
    private LigneTransactionRepository ligneTransactionRepository;
    private final Properties importProperties = new Properties();
    private CSVParser csvParser;
    private int nbrLigneIgnoree;
    private boolean isImportTermine = false;


    public LigneTransactionServiceImpl(LigneTransactionRepository ligneTransactionRepository){
        try(FileInputStream fileInputStream = new FileInputStream("src/main/resources/import.properties")){
            this.ligneTransactionRepository = ligneTransactionRepository;
            this.importProperties.load(fileInputStream);
            this.csvParser = getCSVParser();
        }catch (Exception exception){
            // Remplacer par un logger
            System.out.println(exception.getMessage());
        }
    }

    public Long getNombreDeLignes(){
        return ligneTransactionRepository.count();
    }
    @Override
    public boolean isImportTermine(){
        return isImportTermine;
    }

    @Override
    public Map<String, LigneTransaction> findAllByLocation(PdfLocationDto pdfGenerateDto){
        return getLigneTransactionByLocation(pdfGenerateDto.longitude, pdfGenerateDto.latitude, pdfGenerateDto.rayon);
    }

    public Map<String, LigneTransaction> getLigneTransactionByLocation(Double longitude, Double latitude, Integer rayon){
        if(!isImportTermine){
            throw new ImportNotCompletedException("L'import du fichier CSV n'est pas terminé.");
        }

        Map<String, LigneTransaction> ligneTransactionMap = new HashMap<>();
        int i = 0;
        Double result;

        for(LigneTransaction ligneTransaction : ligneTransactionRepository.findAll()){
            result = calculateHaversineDistance(longitude, latitude, ligneTransaction.getLongitude(), ligneTransaction.getLatitude());
            if (result <= rayon) {
                ligneTransactionMap.put(Integer.toString(i), ligneTransaction);
            }
            i++;
        }
        return ligneTransactionMap;
    }

    public static Double calculateHaversineDistance(Double longitude1, Double latitude1, Double longitude2, Double latitude2) {
        final Integer r = 6371000; // Rayon de la terre en mètres

        // Calcul de la distance de Haversine (Méthode utilisée pour trouver la distance entre 2 points géographiques)
        Double latDistance = degToRad(latitude2 - latitude1);
        Double lonDistance = degToRad(longitude2 - longitude1);
        // Première partie du calcul (la partie intérieure de l'arc tangent)
        Double a = Math.pow(Math.sin(latDistance/2),2) + Math.cos(degToRad(latitude1)) * Math.cos(degToRad(latitude2)) * Math.pow(Math.sin(lonDistance/2),2);
        // Deuxième partie du calcul utilisant le résultat précédent
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // On retourne le résultat du calcul multiplié par le rayon de la Terre afin d'avoir la distance en mètres entre les 2 points
        return r * c;
    }

    public static Double degToRad(Double value) {
        return value * Math.PI / 180;
    }

    @Scheduled(cron = "*/10 * * * * *")
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
                    System.out.println(resultatImport());
                    isImportTermine = true;
                    csvParser.close();
                }
            }
        }catch (Exception exception){

        }
    }

    @Override
    public String getEtatImport(){
        if(isImportTermine){
            return resultatImport();
        }else{
            return getNombreDeLignes() + " lignes ont été importées.";
        }
    }

    private String resultatImport(){
        return "L'ensemble des lignes ont été importées.\n"
                +"Lignes importées : " + this.getNombreDeLignes() + ".\n"
                + "Lignes ignorées : " + this.nbrLigneIgnoree + ".";
    }

    private void importerLigneTransactionByCSVRecord(CSVRecord csvRecord) throws Exception{
        // Si les lignes n'ont pas de longitude/latitude on ne les prends pas (sert a rien)
        if(!csvRecord.get("longitude").isEmpty() && !csvRecord.get("latitude").isEmpty()){
            LigneTransaction ligneTransaction = new LigneTransaction(csvRecord);
            ligneTransactionRepository.save(ligneTransaction);
        }else{
            nbrLigneIgnoree++;
        }
    }

    private CSVParser getCSVParser() throws IOException {
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
