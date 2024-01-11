package fr.esgi.DVF.business;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private double longitude;

    private double latitude;

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

    public LigneTransaction(String idMutation, Date dateMutation, double longitude, double latitude) {
        this.idMutation = idMutation;
        this.dateMutation = dateMutation;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
