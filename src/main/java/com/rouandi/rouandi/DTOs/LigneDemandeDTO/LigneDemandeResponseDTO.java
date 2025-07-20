package com.rouandi.rouandi.DTOs.LigneDemandeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneDemandeResponseDTO {
    private Long id;
    private String nomMateriel;
    private String typeMateriel;
    private String description;
    private String destinataire;
    private String ficheTechnique;
    private int quantite;
    private String etatDemande;
    private String detailsDemande;
    private Date dateDemande;
    private Time timeDemande;
    private String emailCreateur;
}
