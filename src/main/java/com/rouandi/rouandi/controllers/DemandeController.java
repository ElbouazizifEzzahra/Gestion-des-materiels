package com.rouandi.rouandi.controllers;

import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Etat;
import com.rouandi.rouandi.entities.User;
import com.rouandi.rouandi.services.Commande.Demande.DemandeServiceImpl;
import com.rouandi.rouandi.services.Query.Demande.DemandeQueryService;
import com.rouandi.rouandi.services.Query.Demande.DemandeQueryServiceImp;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/demandes")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class DemandeController {

     private final DemandeServiceImpl demandeService;

     private final DemandeQueryServiceImp demandeQueryServiceImp;



    @PreAuthorize("hasAuthority('E')")
    @PostMapping("/add")
    public ResponseEntity<Demande> ajouterDemande(@RequestBody Demande demande) {
        Demande saved = demandeService.addDemande(demande);
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<Demande>>demandes(@PathVariable Long id){
        return ResponseEntity.ok( demandeQueryServiceImp.demandesByUser(id));
    }
    @GetMapping("/filter")
    public ResponseEntity<List<Demande>>filter(
                                               @RequestParam(required = false) Long createurId,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm:ss") Time time,
                                               @RequestParam(required = false) Etat etat
    ){
        List<Demande>demades=demandeQueryServiceImp.filterDemande(etat,date,time,createurId);
        return ResponseEntity.ok(demades);
    }

    @PreAuthorize("hasAuthority('ESI')")
    @PatchMapping("/Update-Etat/{id}")
    public ResponseEntity<?>update(@PathVariable Long id,@RequestParam Etat etat){
        demandeService.update(id,etat);
        return ResponseEntity.ok(Map.of("message","mise a jour avec succes")) ;

    }

    @GetMapping("/demandes")
    public ResponseEntity<List<Demande>>demandes(){
        List<Demande>demades=demandeQueryServiceImp.demandes();
        return ResponseEntity.ok(demades);
    }

}
