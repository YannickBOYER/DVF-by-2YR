package fr.esgi.dvf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class CsvTestFileGenerator {

    public CsvTestFileGenerator(String csvPathFile){
        try (FileWriter writer = new FileWriter(csvPathFile)) {
            // Écriture de l'en-tête CSV
            writer.append("id_mutation,nature_mutation,adresse_suffixe,adresse_nom_voie,adresse_code_voie,code_postal,nom_commune,code_departement,id_parcelle,type_local,date_mutation,adresse_numero,valeur_fonciere,surface_reelle_bati,nombre_pieces_principales,surface_terrain,nombre_lots,longitude,latitude\n");

            // Génération de données aléatoires pour le fichier CSV
            for (int i = 1; i <= 1000; i++) {  // Vous pouvez ajuster le nombre de transactions selon vos besoins
                writer.append("2023-").append(String.valueOf(i)).append(",");
                writer.append(generateRandomString(new String[]{"Vente", "Location", "Succession", "Donation"})).append(",");
                writer.append(generateRandomString(new String[]{"T", "A", "B", "2"})).append(",");  // Exemple pour adresse_suffixe
                writer.append(generateRandomString(new String[]{"ALL DES HETRES", "CHE DE L'AUBEPIN", "CARRE ROCHER", "LES VALETTES"})).append(",");  // Exemple pour adresse_nom_voie
                writer.append(generateRandomString(new String[]{"124", "40", "151", "B112"})).append(",");  // Exemple pour adresse_code_voie
                writer.append(generateRandomString(new String[]{"1130", "1500", "1160", "1340"})).append(",");  // Exemple pour code_postal
                writer.append(generateRandomString(new String[]{"Saint-Genis-Pouilly", "Saint-Jean-de-Niost", "Ambérieu-en-Bugey", "Bourg-en-Bresse"})).append(",");  // Exemple pour nom_commune
                writer.append(generateRandomString(new String[]{"1", "2", "3", "4"})).append(",");  // Exemple pour code_departement
                writer.append(generateRandomString(new String[]{"01354000BD0336", "01004000AV0934", "01348000YB0160", "013140000D2780"})).append(",");  // Exemple pour id_parcelle
                writer.append(generateRandomString(new String[]{"1130", "1500", "1160", "1340"})).append(",");  // Exemple pour type_local
                writer.append(generateRandomDate()).append(",");    // Exemple pour date_mutation
                writer.append(generateRandomString(new String[]{"184", "2914", "427", "41"})).append(",");  // Exemple pour adresse_numero
                writer.append(generateRandomInt(50000, 500000)).append(",");  // Exemple pour valeur_fonciere
                writer.append(generateRandomInt(50, 200)).append(",");  // Exemple pour surface_reelle_bati
                writer.append(generateRandomInt(1, 5)).append(",");  // Exemple pour nombre_pieces_principales
                writer.append(generateRandomInt(100, 500)).append(",");  // Exemple pour surface_terrain
                writer.append(generateRandomInt(1, 3)).append(",");  // Exemple pour nombre_lots
                writer.append(generateRandomLongitude()).append(",");
                writer.append(generateRandomLatitude()).append("\n");
            }

        } catch (IOException e) {
            Logger logger = LogManager.getLogger(CsvTestFileGenerator.class);
            logger.warn(e.getMessage());
        }
    }

    private String generateRandomDate() {
        Random random = new Random();
        long startDate = Timestamp.valueOf("2023-01-01 00:00:00").getTime();
        long endDate = Timestamp.valueOf("2023-12-31 00:00:00").getTime();
        long randomDate = ThreadLocalRandom.current().nextLong(startDate, endDate);
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(randomDate));
    }

    // Méthode pour générer une chaîne de caractères aléatoire (à titre d'exemple)
    private String generateRandomString(String[] tabString) {
        Random random = new Random();
        return tabString[random.nextInt(tabString.length)];
    }

    private String generateRandomInt(int min, int max) {
        Random random = new Random();
        return String.valueOf(min + random.nextInt(max - min + 1));
    }

    // Méthode pour générer une longitude aléatoire
    private String generateRandomLongitude() {
        Random random = new Random();
        double longitude = 4.8357 + (0.1 * random.nextDouble());  // Ajustez la plage selon vos besoins
        return String.format("%.6f", longitude).replace(",", ".");
    }

    // Méthode pour générer une latitude aléatoire
    private String generateRandomLatitude() {
        Random random = new Random();
        double latitude = 45.7640 + (0.1 * random.nextDouble());  // Ajustez la plage selon vos besoins
        return String.format("%.6f", latitude).replace(",", ".");
    }
}
