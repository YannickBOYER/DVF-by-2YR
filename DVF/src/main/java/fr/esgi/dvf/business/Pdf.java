package fr.esgi.dvf.business;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Pdf {
    @Id
    @GeneratedValue
    private Long id;

    private String path;

    public Long getId() {
        return id;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath(){
        return this.path;
    }
}
