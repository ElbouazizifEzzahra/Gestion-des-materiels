package com.rouandi.rouandi.controllers;
import com.rouandi.rouandi.DTOs.LigneDemandeDTO.LigneDemandeRequestDTO;
import com.rouandi.rouandi.services.Commande.Demande.DemandeService;
import com.rouandi.rouandi.services.Commande.LigneDemande.LigneDemandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/lignes-demandes")
public class LigneDemandeController {
    private DemandeService demandeService;
    private LigneDemandeService ligneDemandeService;

    public LigneDemandeController( DemandeService demandeService, LigneDemandeService ligneDemandeService) {
        this.demandeService = demandeService;
        this.ligneDemandeService = ligneDemandeService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> ajouterLignesDemandes(@RequestBody List<LigneDemandeRequestDTO> lignes) {
        try {
            ligneDemandeService.ajouterLignesDemande(lignes);
            return ResponseEntity.ok("Toutes les lignes ont été ajoutées avec succès.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLigneDemande(@PathVariable Long id) {
        ligneDemandeService.supprimerLigneDemande(id);
        return ResponseEntity.ok("Ligne de demande supprimée avec succès");
    }
    @PutMapping("/ligne-demande/{id}")
    public ResponseEntity<String> modifierLigneDemande(
            @PathVariable Long id,
            @RequestBody LigneDemandeRequestDTO dto) {
        try {
            ligneDemandeService.modifierLigneDemande(id, dto);
            return ResponseEntity.ok("Ligne de demande modifiée avec succès");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Erreur : " + ex.getMessage());
        }
    }


}
