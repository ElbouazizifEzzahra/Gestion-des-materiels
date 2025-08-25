
package com.rouandi.rouandi.DTOs.LigneDemandeDTO;
import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.LigneDemande;
import com.rouandi.rouandi.entities.Materiel;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LigneDemandeMapper {

    public static LigneDemande toEntity(LigneDemandeRequestDTO dto, Materiel materiel, Demande demande) {
        LigneDemande ligne = new LigneDemande();
        ligne.setQuantite(dto.getQuantite());
        ligne.setDescription(dto.getDescription());

       // ligne.setFicheTechnique(dto.getFicheTechnique());
        ligne.setMateriel(materiel);
        ligne.setDemande(demande);
        return ligne;
    }

    public static LigneDemandeResponseDTO toDTO(LigneDemande ligneDemande , List<LigneDemande> toutesLignesDeCetteDemande){
        LigneDemandeResponseDTO ligneDemandeResponseDTO=new LigneDemandeResponseDTO();

        ligneDemandeResponseDTO.setNomMateriel(ligneDemande.getMateriel().getNomMateriel());
        ligneDemandeResponseDTO.setTypeMateriel(ligneDemande.getMateriel().getTypeMateriel());
        ligneDemandeResponseDTO.setDescription(ligneDemande.getDescription());
        ligneDemandeResponseDTO.setDestinataire(ligneDemande.getDescription());
        ligneDemandeResponseDTO.setEtatDemande(ligneDemande.getDemande().getEtat().name());
        ligneDemandeResponseDTO.setDateDemande(ligneDemande.getDemande().getDate());
        ligneDemandeResponseDTO.setQuantite(ligneDemande.getQuantite());
        ligneDemandeResponseDTO.setTimeDemande(ligneDemande.getDemande().getTime());
        ligneDemandeResponseDTO.setDecharge(ligneDemande.getDecharge());
        ligneDemandeResponseDTO.setEtat(ligneDemande.getEtat().name());
        ligneDemandeResponseDTO.setFicheTechnique(ligneDemande.getFicheTechnique());
        ligneDemandeResponseDTO.setEmailCreateur(ligneDemande.getDemande().getCreateur().getEmail());
        String details = toutesLignesDeCetteDemande.stream()
                .map(LigneDemande::getDescription)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(" | "));
        ligneDemandeResponseDTO.setDetailsDemande(details);
        return ligneDemandeResponseDTO;

    }
}

