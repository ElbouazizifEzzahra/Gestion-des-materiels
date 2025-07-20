package com.rouandi.rouandi.repositories;

import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Destinataire;
import com.rouandi.rouandi.entities.LigneDemande;
import com.rouandi.rouandi.entities.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LigneDemandeRepository  extends JpaRepository<LigneDemande, Long> {
    LigneDemande save(LigneDemande ligneDemande);
    LigneDemande findLigneDemandeByDemandeAndAndMateriel(Demande demande, Materiel materiel);
    void deleteById(Long id);
    boolean existsLigneDemandeById(Long id);
    Optional<LigneDemande>findById(Long id);
    Optional<LigneDemande> findByMaterielAndDestinataireAndQuantiteAndDescription(
            Materiel materiel,
            Destinataire destinataire,
            int quantite,
            String description
    );

}
