package fr.esgi.dvf.dto;

import java.io.Serializable;

public class PdfLocationDto implements Serializable {
    public final Double longitude;

    public final Double latitude;

    public final Integer rayon;

    public final Long idPdf;

    public PdfLocationDto(Double longitude, Double latitude, Integer rayon, Long idPdf) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
        this.rayon = rayon;
        this.idPdf = idPdf;
    }
}
