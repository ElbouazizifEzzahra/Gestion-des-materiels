package com.rouandi.rouandi.DTOs.MaterielDTO;

import com.rouandi.rouandi.entities.Materiel;

public class MaterielMapper {
    public static MaterielResponseDTO toDTO(Materiel materiel){
        MaterielResponseDTO dto = new MaterielResponseDTO();
        dto.setNomMateriel(materiel.getNomMateriel());
        dto.setTypeMateriel(materiel.getTypeMateriel());
        dto.setIdMateriel(materiel.getIdMateriel());
        return dto;
    }
    public static Materiel fromDTO(MaterielRequestDTO  materielRequestDTO){
    Materiel materiel=new Materiel();
    materiel.setNomMateriel(materielRequestDTO.getNomMateriel());
    materiel.setTypeMateriel(materielRequestDTO.getTypeMateriel());
    return materiel;
    }
}
