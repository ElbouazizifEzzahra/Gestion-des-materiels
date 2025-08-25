package com.rouandi.rouandi.controllers;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouandi.rouandi.DTOs.DechargeDTO.DechargeDTO;
import com.rouandi.rouandi.DTOs.LigneDemandeDTO.LigneDemandeRequestDTO;
import com.rouandi.rouandi.entities.EtatLigne;
import com.rouandi.rouandi.entities.TypeLivraison;
import com.rouandi.rouandi.services.Commande.Demande.DemandeService;
import com.rouandi.rouandi.services.Commande.LigneDemande.LigneDemandeService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/lignes-demandes")
@CrossOrigin(origins ="http://localhost:4200")
public class LigneDemandeController {
    private DemandeService demandeService;
    private LigneDemandeService ligneDemandeService;

    public LigneDemandeController(DemandeService demandeService, LigneDemandeService ligneDemandeService) {
        this.demandeService = demandeService;
        this.ligneDemandeService = ligneDemandeService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> ajouterLignesDemande(  @RequestPart("lignes") String lignesJson,
                                                    @RequestPart(value = "fichesTechniques", required = false) List<MultipartFile> fichiers
    ) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<LigneDemandeRequestDTO> lignes = mapper.readValue(
                    lignesJson,
                    new TypeReference<List<LigneDemandeRequestDTO>>() {}
            );

            ligneDemandeService.ajouterLignesDemande(lignes, fichiers);

            return ResponseEntity.ok(Map.of("message", "Toutes les lignes ont été ajoutées avec succès."));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }


    @GetMapping(value = "/ligne/{idLigne}")
    public ResponseEntity<byte[]> genererDechargeDepuisLigne(@PathVariable Long idLigne,
                                                             @RequestParam String ville,
                                                             @RequestParam List<String> caracteristiques) {
        String[] caractArray = caracteristiques.toArray(new String[0]);
        DechargeDTO dto = ligneDemandeService.construireDTODepuisLigne(idLigne, caractArray, ville);
        byte[] pdfBytes = ligneDemandeService.generateDecharge(dto);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=decharge.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteLigneDemande(@PathVariable Long id) {
        ligneDemandeService.supprimerLigneDemande(id);
        return ResponseEntity.ok(Map.of("message","Ligne de demande supprimée avec succès"));
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> modifierLigneDemande(
            @PathVariable Long id,
            @RequestPart("dto") LigneDemandeRequestDTO dto,
            @RequestPart(value = "ficheTechnique", required = false) MultipartFile fichier
    ) {
        try {
            ligneDemandeService.modifierLigneDemande(id, dto,fichier);
            return ResponseEntity.ok(Map.of("message","Ligne de demande modifiée avec succès"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Erreur : " + ex.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/email/{id}")
    public ResponseEntity<?> emailAchat(@PathVariable Long id) {
        ligneDemandeService.emailAchat(id);
        return ResponseEntity.ok(Map.of("message", "Email bien envoye"));
    }

    @PostMapping("/decharge/{id}")
    public ResponseEntity<?> decharge(@PathVariable Long id,
                                           @RequestPart(value = "decharge", required = false) MultipartFile fichier) throws IOException {
        ligneDemandeService.ajouterDecharge(fichier, id);
        return ResponseEntity.ok(Map.of("message", "la fiche de decharge a été ajouté avec succès."));
    }

    @GetMapping("/decharge/{filename}")
    public ResponseEntity<Resource> getDecharge(@PathVariable String filename, Authentication auth) throws MalformedURLException {

        Path filePath = Paths.get("C:/app/decharges").resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @PostMapping("/livraison/{id}")
    public ResponseEntity<?> ajouterLivraison(
            @PathVariable Long id,
            @RequestParam String type) {

        try {
            TypeLivraison typeEnum = TypeLivraison.valueOf(type.toUpperCase());
            ligneDemandeService.ajouterLivraision(id, typeEnum);
            return ResponseEntity.ok(Map.of("message", "Livraison ajoutée avec succès"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Type de livraison invalide"));
        }
    }

    @PatchMapping("/Update-Etat/{id}")
    public ResponseEntity<?>update(@PathVariable Long id,@RequestParam EtatLigne etat){
       ligneDemandeService.update(id,etat);
        return ResponseEntity.ok(Map.of("message","mise a jour avec succes")) ;

    }
}
