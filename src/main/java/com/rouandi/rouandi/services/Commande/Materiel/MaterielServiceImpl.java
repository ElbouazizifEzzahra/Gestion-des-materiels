package com.rouandi.rouandi.services.Commande.Materiel;

import com.rouandi.rouandi.entities.Materiel;
import com.rouandi.rouandi.repositories.MaterielRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Transactional
@Service
public class MaterielServiceImpl implements MaterielService {

    private MaterielRepository materielRepository;

    public MaterielServiceImpl(MaterielRepository materielRepository) {
        this.materielRepository = materielRepository;
    }

    @Override
    public Materiel ajouterMateriel(Materiel materiel) {
        return materielRepository
                .findByNomMaterielAndTypeMateriel(
                        materiel.getNomMateriel(),
                        materiel.getTypeMateriel()
                )
                .orElseGet(() -> materielRepository.save(materiel));
    }

    @Override
    public void deleteMateriel(Long id) {
        if (materielRepository.existsMaterielByIdMateriel(id))
            materielRepository.deleteMaterielByIdMateriel(id);
        else
            System.out.println("le materiel n'existe pas ");

    }

    @Override
    public Materiel updateMateriel(Long id, String nom, String type) {
        Optional<Materiel> optionalMateriel = materielRepository.findById(id);

        if (optionalMateriel.isPresent()) {
            Materiel materiel = optionalMateriel.get();

            if (nom != null) {
                materiel.setNomMateriel(nom);
            }
            if (type != null) {
                materiel.setTypeMateriel(type);
            }


            return materielRepository.save(materiel);
        }


        throw new RuntimeException("Le matériel avec l'ID " + id + " n'existe pas.");
    }


    }
