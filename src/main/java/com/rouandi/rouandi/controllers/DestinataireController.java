package com.rouandi.rouandi.controllers;

import com.rouandi.rouandi.entities.Destinataire;
import com.rouandi.rouandi.services.Commande.Destinataire.DestinataireService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/destinataire")
@AllArgsConstructor
public class DestinataireController {
    private DestinataireService destinataireService;
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?>deleteDestinataire(@RequestBody Destinataire destinataire){
        destinataireService.supprimerDestinataire(destinataire);
        return ResponseEntity.ok(Map.of("message","le destinataire a bien supprime")) ;
    }
}
