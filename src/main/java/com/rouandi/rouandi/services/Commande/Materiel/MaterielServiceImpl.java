package com.rouandi.rouandi.services.Commande.Materiel;

import com.rouandi.rouandi.entities.Materiel;
import com.rouandi.rouandi.repositories.MaterielRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
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
        if(materielRepository.existsMaterielByIdMateriel(id))
            materielRepository.deleteMaterielByIdMateriel(id);
        else
            System.out.println("le materiel n'existe pas ");

    }
    @Override
    public void updateMateriel(Long id, String type, String nom) {

        if(materielRepository.existsMaterielByIdMateriel(id))
        {
            if(type!=null && nom!=null){
                materielRepository.updateMaterielByIdAndNomMaterielAndTypeMateriel(id,nom,type);
            }
            if(type!=null )
                materielRepository.updateMaterielByIdAndTypeMateriel(id,type);
            if(nom!=null)
                materielRepository.updateMaterielByIdAndNomMateriel(id,nom);
        }
    }
}
