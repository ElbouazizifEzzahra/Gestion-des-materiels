package com.rouandi.rouandi.DTOs.LigneDemandeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneDemandeRequestDTO {
    private Integer fileIndex;

    private int quantite;
    private Long materielId;
    private String nomMateriel;
    private String typeMateriel;
    private String description;
    private String decharge;
    private String nomDestinataire;
    private String fonctionDestinataire;
    private String typeDestinataire;
    private String prenomDestinataire;
    private String emailDestinataire;
    private String projetDestinataire;
    private String ficheTechnique;
    private String etat;
    private String typeLivraision;


}
