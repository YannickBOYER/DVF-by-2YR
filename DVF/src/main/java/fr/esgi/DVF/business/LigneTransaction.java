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

    private Long idMutation;

    private Date dateMutation;
}
