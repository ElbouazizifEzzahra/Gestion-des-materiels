package com.rouandi.rouandi.controllers;

import com.rouandi.rouandi.entities.Destinataire;
import com.rouandi.rouandi.services.Commande.Destinataire.DestinataireService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/destinataire")
@AllArgsConstructor
public class DestinataireController {
    private DestinataireService destinataireService;
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDestinataire(@PathVariable Long id) {
        destinataireService.supprimerDestinataireParId(id);
        return ResponseEntity.ok(Map.of("message", "Le destinataire a bien été supprimé"));
    }
}
