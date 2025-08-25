package com.rouandi.rouandi.services.Query.Materiel;

import com.rouandi.rouandi.DTOs.MaterielDTO.MaterielResponseDTO;
import com.rouandi.rouandi.entities.Materiel;

import java.util.List;

public interface MaterielQueryService {
    public List<Materiel> filter(String nom, String type);
    public List<MaterielResponseDTO> getMaterielSimpleInfo();
    public List<Materiel> filter(String nom);
}
