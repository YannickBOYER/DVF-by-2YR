package fr.esgi.dvf.service.impl;

import fr.esgi.dvf.business.LigneTransaction;
import fr.esgi.dvf.exception.ImportNotCompletedException;
import fr.esgi.dvf.repository.LigneTransactionRepository;
import fr.esgi.dvf.service.LigneTransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.Map;

@Service
public class LigneTransactionServiceImpl implements LigneTransactionService {

    private final Logger logger = LogManager.getLogger(LigneTransactionServiceImpl.class);

    private LigneTransactionRepository ligneTransactionRepository;
    private CSVParser csvParser;
    private int nbrLigneIgnoree;
    private boolean isImportCompleted = false;

    @Value("${localFilePath}")
    private String localFilePath;

    @Value("${nbrIntervalLigneParImport}")
    private int nbrIntervalLigneParImport;

    @Value("${csvCompresedFileUrlPath}")
    private String csvCompresedFileUrlPath;


    public LigneTransactionServiceImpl(LigneTransactionRepository ligneTransactionRepository) {
        this.ligneTransactionRepository = ligneTransactionRepository;
    }

    @Bean
    public void init() {
        try {
            downloadAndDecompressGzip();
            this.csvParser = getCSVParser(localFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Long getNombreDeLignes() {
        return ligneTransactionRepository.count();
    }

    @Override
    public boolean isImportCompleted() {
        return isImportCompleted;
    }

    @Override
    public Map<String, LigneTransaction> findAllByLocation(Double longitude, Double latitude, Integer rayon) {
        /*if (!isImportCompleted) {
            throw new ImportNotCompletedException("L'import du fichier CSV n'est pas terminé.");
        }*/

        Map<String, LigneTransaction> ligneTransactionMap = new HashMap<>();
        int i = 0;
        double result;

        for (LigneTransaction ligneTransaction : ligneTransactionRepository.findAll()) {
            result = calculateHaversineDistance(longitude, latitude, ligneTransaction.getLongitude(), ligneTransaction.getLatitude());
            if (result <= rayon) {
                ligneTransactionMap.put(Integer.toString(i), ligneTransaction);
            }
            i++;
        }
        return ligneTransactionMap;
    }

    public static Double calculateHaversineDistance(Double longitude1, Double latitude1, Double longitude2, Double latitude2) {
        final int r = 6371000; // Rayon de la terre en mètres

        // Calcul de la distance de Haversine (Méthode utilisée pour trouver la distance entre 2 points géographiques)
        Double latDistance = degToRad(latitude2 - latitude1);
        Double lonDistance = degToRad(longitude2 - longitude1);
        // Première partie du calcul (la partie intérieure de l'arc tangent)
        double a = Math.pow(Math.sin(latDistance / 2), 2) + Math.cos(degToRad(latitude1)) * Math.cos(degToRad(latitude2)) * Math.pow(Math.sin(lonDistance / 2), 2);
        // Deuxième partie du calcul utilisant le résultat précédent
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // On retourne le résultat du calcul multiplié par le rayon de la Terre afin d'avoir la distance en mètres entre les 2 points
        return r * c;
    }

    public static Double degToRad(Double value) {
        return value * Math.PI / 180;
    }

    @Scheduled(cron = "*/10 * * * * *")
    //@Scheduled(cron = "0 */5 * * * *")
    public void importSheduled() throws Exception {
        importByNbrInterval(nbrIntervalLigneParImport);
    }

    public void importByNbrInterval(int nbrIntervalLigne) throws Exception {
        if (!isImportCompleted) {
            if (csvParser.iterator().hasNext()) {
                for (int i = 0; i < nbrIntervalLigne; i++) {
                    if (csvParser.iterator().hasNext()) {
                        CSVRecord csvRecord = csvParser.iterator().next();
                        importerLigneTransactionByCSVRecord(csvRecord);
                    }
                }
                logger.info(getEtatImport());
            } else {
                isImportCompleted = true;
                csvParser.close();
                logger.warn(getEtatImport());
            }
        }
    }

    public String getEtatImport() {
        if (isImportCompleted) {
            return resultatImport();
        } else {
            return getNombreDeLignes() + " lignes ont été importées.";
        }
    }

    private String resultatImport() {
        return "L'ensemble des lignes ont été importées.\n"
                + "Lignes importées : " + this.getNombreDeLignes() + ".\n"
                + "Lignes ignorées : " + this.nbrLigneIgnoree + ".";
    }

    public void importerLigneTransactionByCSVRecord(CSVRecord csvRecord) throws ParseException {
        // Si les lignes n'ont pas de longitude/latitude on ne les prends pas
        if (!csvRecord.get("longitude").isEmpty() && !csvRecord.get("latitude").isEmpty()) {
            LigneTransaction ligneTransaction = new LigneTransaction(csvRecord);
            ligneTransactionRepository.save(ligneTransaction);
        } else {
            nbrLigneIgnoree++;
        }
    }

    public CSVParser getCSVParser(String csvFilePath) throws IOException {
        File file = new File(csvFilePath);
        Reader reader = new InputStreamReader(new BufferedInputStream(file.toURI().toURL().openStream()));
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

    public int getNbrIntervalLigneParImport() {
        return nbrIntervalLigneParImport;
    }
}
