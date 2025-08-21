package com.rouandi.rouandi.services.Commande.LigneDemande;

import com.rouandi.rouandi.DTOs.DechargeDTO.DechargeDTO;
import com.rouandi.rouandi.DTOs.LigneDemandeDTO.LigneDemandeRequestDTO;
import com.rouandi.rouandi.entities.Etat;
import com.rouandi.rouandi.entities.EtatLigne;
import com.rouandi.rouandi.entities.TypeLivraison;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LigneDemandeService {
    public void ajouterLignesDemande(List<LigneDemandeRequestDTO> lignes,List<MultipartFile> fichiers) throws IOException;
    public void supprimerLigneDemande(Long id);
    //public void modifierLigneDemande(Long idLigne, LigneDemandeRequestDTO dto);
//    public List<LigneDemande> creerDemandeAvecLignes(List<LigneDemandeRequestDTO> lignesDTO);
    public byte[] generateDecharge(DechargeDTO dto);
    public DechargeDTO construireDTODepuisLigne(Long idLigne, String[] caract ,String ville);
    public void emailAchat(Long idLigne);
    public void ajouterDecharge(MultipartFile fichier,Long idLigne)throws IOException ;
    public void modifierLigneDemande(Long idLigne, LigneDemandeRequestDTO dto,MultipartFile nouvelleFiche) throws IOException;

    void update(Long id, EtatLigne etat);
    void ajouterLivraision(Long id, TypeLivraison type);
    public void actualiserDuree();
    public void incrementerDureeParEtat();

}
