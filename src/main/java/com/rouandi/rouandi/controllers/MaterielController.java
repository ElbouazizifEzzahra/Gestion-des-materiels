package com.rouandi.rouandi.controllers;

import com.rouandi.rouandi.DTOs.MaterielDTO.MaterielMapper;
import com.rouandi.rouandi.DTOs.MaterielDTO.MaterielRequestDTO;
import com.rouandi.rouandi.DTOs.MaterielDTO.MaterielResponseDTO;
import com.rouandi.rouandi.entities.Materiel;
import com.rouandi.rouandi.services.Commande.Materiel.MaterielServiceImpl;
import com.rouandi.rouandi.services.Query.Materiel.MaterielQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/materiel")
@CrossOrigin(origins = "http://localhost:4200")
public class MaterielController {
    private MaterielServiceImpl materielService;
    private MaterielMapper materielMapper;
    private MaterielQueryService materielQueryService;

    public MaterielController(MaterielServiceImpl materielService, MaterielQueryService materielQueryService) {
        this.materielService = materielService;
        this.materielQueryService = materielQueryService;
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<MaterielResponseDTO> ajouterMateriel(@RequestBody MaterielRequestDTO materielRequestDTO){
        Materiel materiel =MaterielMapper.fromDTO(materielRequestDTO);
        Materiel saved= materielService.ajouterMateriel(materiel);
    return ResponseEntity.ok(MaterielMapper.toDTO(saved));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMateriel(@PathVariable Long id) {
        materielService.deleteMateriel(id);
        return ResponseEntity.ok(Map.of("message","Matériel supprimé avec succès."));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> modifier(
            @PathVariable Long id,
            @RequestParam(name = "nomMateriel", required = false) String nom,
            @RequestParam(name = "typeMateriel", required = false) String type) {

        materielService.updateMateriel(id, nom, type);
        return ResponseEntity.ok(Map.of("message", "bien fait"));
    }


@GetMapping("/filter")
public ResponseEntity<List<Materiel>> filter(
        @RequestParam(required = false) String nom) {
    List<Materiel> materiels = materielQueryService.filter(nom);
    return ResponseEntity.ok(materiels);
}
    @GetMapping("/materiels")
    public ResponseEntity<List<MaterielResponseDTO>> getMaterielSimple() {
        return ResponseEntity.ok(materielQueryService.getMaterielSimpleInfo());
    }

}
