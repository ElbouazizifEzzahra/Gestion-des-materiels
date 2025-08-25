package com.rouandi.rouandi.DTOs.DemadeDTO;

import com.rouandi.rouandi.DTOs.DestinataireDTO.DestinataireRequest;
import com.rouandi.rouandi.DTOs.LigneDemandeDTO.LigneDemandeRequestDTO;
import com.rouandi.rouandi.DTOs.LigneDemandeDTO.LigneDemandeResponseDTO;
import com.rouandi.rouandi.DTOs.MaterielDTO.MaterielRequestDTO;
import com.rouandi.rouandi.DTOs.UserDTO.UserRequestDTO;
import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Destinataire;
import com.rouandi.rouandi.entities.Materiel;
import com.rouandi.rouandi.entities.User;
import lombok.*;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DemandeResponseDTO {
    private Long idDemande;
    private String etat;
    private Date date;
    private Time time;
    private String nomCreateur;

    private List<LigneDemandeResponseDTO> demandes;
    }


