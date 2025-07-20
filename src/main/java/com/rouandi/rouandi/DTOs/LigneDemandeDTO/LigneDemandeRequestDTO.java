package com.rouandi.rouandi.DTOs.LigneDemandeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneDemandeRequestDTO {
    private int quantite;
    private String nomMateriel;
    private String typeMateriel;
    private String description;
    private String ficheTechnique;
    private String nomDestinataire;
    private String typeDestinataire;
    private String prenomDestinataire;
    private String emailDestinataire;
    private String projetDestinataire;


}
