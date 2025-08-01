package com.rouandi.rouandi.services.Query.Materiel;

import com.rouandi.rouandi.DTOs.MaterielDTO.MaterielResponseDTO;
import com.rouandi.rouandi.entities.Materiel;
import com.rouandi.rouandi.repositories.MaterielRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterielQueryServiceImp implements MaterielQueryService {
    private MaterielRepository materielRepository;

    public MaterielQueryServiceImp(MaterielRepository materielRepository) {
        this.materielRepository = materielRepository;
    }

    @Override
    public List<Materiel> filter(String nom, String type) {
        if (nom != null && type != null) {
            return materielRepository.findMaterielByNomMaterielAndTypeMateriel(nom, type);
        } else if (nom != null) {
            return materielRepository.findMaterielByNomMateriel(nom);
        } else if (type != null) {
            return materielRepository.findMaterielByTypeMateriel(type);
        } else {
            return materielRepository.findAll();
        }
    }
    @Override
    public List<MaterielResponseDTO> getMaterielSimpleInfo() {
        List<Materiel> materiels = materielRepository.findAll();
        return materiels.stream()
                .map(m -> new MaterielResponseDTO(
                        m.getIdMateriel(),
                        m.getNomMateriel(),
                        m.getTypeMateriel()
                ))
                .toList();
    }
    public List<Materiel> filter(String nom) {
        if (nom == null || nom.isEmpty()) {
            return materielRepository.findAll();
        }
        return materielRepository.findByNomMaterielStartingWithIgnoreCase(nom);
    }

}
