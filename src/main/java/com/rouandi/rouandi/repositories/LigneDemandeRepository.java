package com.rouandi.rouandi.repositories;

import com.rouandi.rouandi.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Modifying
    @Query("UPDATE LigneDemande d SET d.etat = :etat WHERE d.id = :id")
    void updateEtatById(@Param("id") Long id, @Param("etat") EtatLigne etat);

}
