package com.rouandi.rouandi.DTOs.DestinataireDTO;

import com.rouandi.rouandi.entities.Type;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinataireRequest {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String projet;
    private String fonction;
    private String type;
}
