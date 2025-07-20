package com.rouandi.rouandi.services.Query.Materiel;

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
}
