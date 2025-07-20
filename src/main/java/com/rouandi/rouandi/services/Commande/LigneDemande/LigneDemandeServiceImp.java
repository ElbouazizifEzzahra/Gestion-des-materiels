package com.rouandi.rouandi.services.Commande.LigneDemande;

import com.rouandi.rouandi.DTOs.DestinataireDTO.DestinataireMapper;
import com.rouandi.rouandi.DTOs.LigneDemandeDTO.LigneDemandeRequestDTO;
import com.rouandi.rouandi.entities.*;
import com.rouandi.rouandi.repositories.*;
import com.rouandi.rouandi.services.Commande.Demande.DemandeService;
import com.rouandi.rouandi.services.Commande.User.UserServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class LigneDemandeServiceImp implements LigneDemandeService {
    private LigneDemandeRepository ligneDemandeRepository;
    private DestinataireRepository destinataireRepository;
    private DemandeService demandeService;
    private MaterielRepository materielRepository;
    private DemandeRepository demandeRepository;
    private UserServiceImp userServiceImp;


    @Override
    public void ajouterLignesDemande(List<LigneDemandeRequestDTO> lignes) {
        if (lignes == null || lignes.isEmpty()) {
            throw new IllegalArgumentException("La liste de lignes ne doit pas être vide");
        }
        Demande demande = new Demande();
        demande.setEtat(Etat.ENVOYE);
        demande.setDate(new Date());
        demande.setTime(new Time(System.currentTimeMillis()));
        demande.setCreateur(userServiceImp.getCurrentUser());
        demandeRepository.save(demande);

        for (LigneDemandeRequestDTO dto : lignes) {
            Materiel materiel = materielRepository.findByNomMaterielAndTypeMateriel(dto.getNomMateriel(), dto.getTypeMateriel())
                    .orElseThrow(() -> new IllegalArgumentException("Matériel non trouvé : " + dto.getNomMateriel()));
            Destinataire destinataire = destinataireRepository
                    .findDestinataireByEmail(dto.getEmailDestinataire())
                    .orElseGet(() -> destinataireRepository.save(DestinataireMapper.toEntityFromDTO(dto)));


            Optional<LigneDemande> existante = ligneDemandeRepository
                    .findByMaterielAndDestinataireAndQuantiteAndDescription(
                            materiel, destinataire, dto.getQuantite(), dto.getDescription());

            if (existante.isPresent()) {
                throw new IllegalArgumentException("Doublon détecté : ligne avec " + dto.getNomMateriel() + " déjà existante.");
            }

            LigneDemande ligne = new LigneDemande();
            ligne.setDemande(demande);
            ligne.setMateriel(materiel);
            ligne.setDestinataire(destinataire);
            ligne.setQuantite(dto.getQuantite());
            ligne.setDescription(dto.getDescription());
            ligne.setFicheTechnique(dto.getFicheTechnique());


            ligneDemandeRepository.save(ligne);
        }
    }

    @Override
    public void supprimerLigneDemande(Long id) {
        if (ligneDemandeRepository.existsLigneDemandeById(id))
            ligneDemandeRepository.deleteById(id);
        else
            System.out.println("la ligne n'existe pas");

    }

    @Override
    public void modifierLigneDemande(Long idLigne, LigneDemandeRequestDTO dto) {
        LigneDemande ligne = ligneDemandeRepository.findById(idLigne)
                .orElseThrow(() -> new RuntimeException("LigneDemande introuvable"));

        if (dto.getQuantite() > 0) {
            ligne.setQuantite(dto.getQuantite());
        }

        if (dto.getNomMateriel() != null && !dto.getNomMateriel().isBlank()) {
            ligne.getMateriel().setNomMateriel(dto.getNomMateriel());
        }

        if (dto.getTypeMateriel() != null && !dto.getTypeMateriel().isBlank()) {
            ligne.getMateriel().setTypeMateriel(dto.getTypeMateriel());
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            ligne.setDescription(dto.getDescription());
        }

        if (dto.getFicheTechnique() != null && !dto.getFicheTechnique().isBlank()) {
            ligne.setFicheTechnique(dto.getFicheTechnique());
        }


        Destinataire dest = ligne.getDestinataire();
        if (dto.getNomDestinataire() != null && !dto.getNomDestinataire().isBlank()) {
            dest.setNom(dto.getNomDestinataire());
        }

        if (dto.getPrenomDestinataire() != null && !dto.getPrenomDestinataire().isBlank()) {
            dest.setPrenom(dto.getPrenomDestinataire());
        }

        if (dto.getEmailDestinataire() != null && !dto.getEmailDestinataire().isBlank()) {
            dest.setEmail(dto.getEmailDestinataire());
        }

        if (dto.getProjetDestinataire() != null && !dto.getProjetDestinataire().isBlank()) {
            dest.setProjet(dto.getProjetDestinataire());
        }

        if (dto.getTypeDestinataire() != null && !dto.getTypeDestinataire().isBlank()) {
            dest.setType(Type.valueOf(dto.getTypeDestinataire()));
        }


        ligneDemandeRepository.save(ligne);
    }
}