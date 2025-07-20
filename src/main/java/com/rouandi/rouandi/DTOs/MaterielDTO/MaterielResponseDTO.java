package com.rouandi.rouandi.DTOs.MaterielDTO;

import com.rouandi.rouandi.entities.LigneDemande;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterielResponseDTO {
    private String nomMateriel;
    private String typeMateriel;
    private List<LigneDemande> ligneDmandes;
}
