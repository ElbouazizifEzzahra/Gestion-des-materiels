package com.rouandi.rouandi.DTOs.DemadeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandeRequestDTO {
    private Long id;
    private String nom;
    private String adresse;
}
