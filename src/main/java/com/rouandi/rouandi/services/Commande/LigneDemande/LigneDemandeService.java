package com.rouandi.rouandi.services.Commande.LigneDemande;

import com.rouandi.rouandi.DTOs.LigneDemandeDTO.LigneDemandeRequestDTO;
import com.rouandi.rouandi.entities.LigneDemande;

import java.util.List;

public interface LigneDemandeService {
    public void ajouterLignesDemande(List<LigneDemandeRequestDTO> lignes);
    public void supprimerLigneDemande(Long id);
    public void modifierLigneDemande(Long idLigne, LigneDemandeRequestDTO dto);
//    public List<LigneDemande> creerDemandeAvecLignes(List<LigneDemandeRequestDTO> lignesDTO);
}
