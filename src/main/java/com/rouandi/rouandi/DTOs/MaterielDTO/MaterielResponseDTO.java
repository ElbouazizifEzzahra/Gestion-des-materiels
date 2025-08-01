package com.rouandi.rouandi.DTOs.MaterielDTO;

import com.rouandi.rouandi.entities.LigneDemande;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MaterielResponseDTO {
    private  Long idMateriel;
    private String nomMateriel;
    private String typeMateriel;

}
