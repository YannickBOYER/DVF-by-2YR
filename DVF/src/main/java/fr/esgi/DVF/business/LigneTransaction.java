package fr.esgi.DVF.business;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
public class LigneTransaction {
    @Id
    @GeneratedValue
    private Long id;
    private String idMutation;
    private Date dateMutation;
    private String natureMutation;
    private Float valeurFonciere;
    private Integer adresseNumero;
    private String adresseSuffixe;
    private String adresseNomVoie;
    private String adresseCodeVoie;
    private String codePostal;
    private String nomCommune;
    private String codeDepartement;
    private String idParcelle;
    private Integer nombreLots;
    private String typeLocal;
    private Integer surfaceReelleBati;
    private Integer nombrePiecesPrincipales;
    private Integer surfaceTerrain;
    private double longitude;
    private double latitude;

    public LigneTransaction(String idMutation, Date dateMutation, String natureMutation, Float valeurFonciere, Integer adresseNumero, String adresseSuffixe, String adresseNomVoie, String adresseCodeVoie, String codePostal, String nomCommune, String codeDepartement, String idParcelle, Integer nombreLots, String typeLocal, Integer surfaceReelleBati, Integer nombrePiecesPrincipales, Integer surfaceTerrain, double longitude, double latitude) {
        this.idMutation = idMutation;
        this.dateMutation = dateMutation;
        this.natureMutation = natureMutation;
        this.valeurFonciere = valeurFonciere;
        this.adresseNumero = adresseNumero;
        this.adresseSuffixe = adresseSuffixe;
        this.adresseNomVoie = adresseNomVoie;
        this.adresseCodeVoie = adresseCodeVoie;
        this.codePostal = codePostal;
        this.nomCommune = nomCommune;
        this.codeDepartement = codeDepartement;
        this.idParcelle = idParcelle;
        this.nombreLots = nombreLots;
        this.typeLocal = typeLocal;
        this.surfaceReelleBati = surfaceReelleBati;
        this.nombrePiecesPrincipales = nombrePiecesPrincipales;
        this.surfaceTerrain = surfaceTerrain;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getIdMutation() {
        return idMutation;
    }
    public Date getDateMutation() {
        return dateMutation;
    }
    public String getNatureMutation() {
        return natureMutation;
    }
    public Float getValeurFonciere() {
        return valeurFonciere;
    }
    public String getIdParcelle() {
        return idParcelle;
    }
    public Integer getNombreLots() {
        return nombreLots;
    }
    public String getTypeLocal() {
        return typeLocal;
    }
    public Integer getSurfaceReelleBati() {
        return surfaceReelleBati;
    }
    public Integer getNombrePiecesPrincipales() {
        return nombrePiecesPrincipales;
    }
    public Integer getSurfaceTerrain() {
        return surfaceTerrain;
    }

    public String getAdresse(){
        return  this.adresseNumero +
                this.adresseSuffixe + " " +
                this.adresseNomVoie + " " +
                this.adresseCodeVoie + ", " +
                this.codePostal + " " +
                this.nomCommune + " " +
                this.codeDepartement;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /// Import CSV
    public LigneTransaction(CSVRecord csvRecord) throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String idMutation = csvRecord.get("id_mutation");
        String natureMutation = csvRecord.get("nature_mutation");
        String adresseSuffixe = csvRecord.get("adresse_suffixe");
        String adresseNomVoie = csvRecord.get("adresse_nom_voie");
        String adresseCodeVoie = csvRecord.get("adresse_code_voie");
        String codePostal = csvRecord.get("code_postal");
        String nomCommune = csvRecord.get("nom_commune");
        String codeDepartement = csvRecord.get("code_departement");
        String idParcelle = csvRecord.get("id_parcelle");
        String typeLocal = csvRecord.get("type_local");
        String strDateMutation = csvRecord.get("date_mutation");
        String strAdresseNumero = csvRecord.get("adresse_numero");
        String strValeurFonciere = csvRecord.get("valeur_fonciere");
        String strSurfaceReelleBati = csvRecord.get("surface_reelle_bati");
        String strNombrePiecesPrincipales = csvRecord.get("nombre_pieces_principales");
        String strSurfaceTerrain = csvRecord.get("surface_terrain");
        String strNombreLots = csvRecord.get("nombre_lots");

        Date dateMutation = null;
        Integer adresseNumero = null;
        Float valeurFonciere = null;
        Integer surfaceReelleBati = null;
        Integer nombrePiecesPrincipales = null;
        Integer surfaceTerrain = null;
        Integer nombreLots = null;

        if(!strDateMutation.isEmpty() && strDateMutation.matches("-?\\d+(\\.\\d+)?"))
            dateMutation = dateFormat.parse(strDateMutation);
        if(!strAdresseNumero.isEmpty() && strAdresseNumero.matches("-?\\d+(\\.\\d+)?"))
            adresseNumero = Integer.parseInt(strAdresseNumero);
        if(!strValeurFonciere.isEmpty() && strValeurFonciere.matches("-?\\d+(\\.\\d+)?"))
            valeurFonciere = Float.parseFloat(strValeurFonciere);
        if(!strSurfaceReelleBati.isEmpty() && strSurfaceReelleBati.matches("-?\\d+(\\.\\d+)?"))
            surfaceReelleBati = Integer.parseInt(strSurfaceReelleBati);
        if(!strNombrePiecesPrincipales.isEmpty() && strNombrePiecesPrincipales.matches("-?\\d+(\\.\\d+)?"))
            nombrePiecesPrincipales = Integer.parseInt(strNombrePiecesPrincipales);
        if(!strSurfaceTerrain.isEmpty() && strSurfaceTerrain.matches("-?\\d+(\\.\\d+)?"))
            surfaceTerrain = Integer.parseInt(strSurfaceTerrain);
        if(!strNombreLots.isEmpty() && strNombreLots.matches("-?\\d+(\\.\\d+)?"))
            nombreLots = Integer.parseInt(strNombreLots);

        double longitude = Double.parseDouble(csvRecord.get("longitude"));
        double latitude = Double.parseDouble(csvRecord.get("latitude"));

        this.idMutation = idMutation;
        this.dateMutation = dateMutation;
        this.natureMutation = natureMutation;
        this.valeurFonciere = valeurFonciere;
        this.adresseNumero = adresseNumero;
        this.adresseSuffixe = adresseSuffixe;
        this.adresseNomVoie = adresseNomVoie;
        this.adresseCodeVoie = adresseCodeVoie;
        this.codePostal = codePostal;
        this.nomCommune = nomCommune;
        this.codeDepartement = codeDepartement;
        this.idParcelle = idParcelle;
        this.nombreLots = nombreLots;
        this.typeLocal = typeLocal;
        this.surfaceReelleBati = surfaceReelleBati;
        this.nombrePiecesPrincipales = nombrePiecesPrincipales;
        this.surfaceTerrain = surfaceTerrain;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
