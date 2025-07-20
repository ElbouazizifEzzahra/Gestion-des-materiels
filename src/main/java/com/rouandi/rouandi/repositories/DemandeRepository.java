package com.rouandi.rouandi.repositories;

import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Etat;
import com.rouandi.rouandi.entities.Materiel;
import com.rouandi.rouandi.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.util.Date;
import java.util.List;

@Transactional
@Repository
public interface DemandeRepository extends JpaRepository<Demande,Long> {
    //boolean findDemandeByByMateriel(Materiel materiel);

    void deleteById(Long id);
    @Modifying
    @Query("UPDATE Demande d SET d.etat = :etat WHERE d.createur.id= :id")
    void updateDemandeByIdandEtat(@Param("etat")Etat etat , @Param("id")Long id );
    List<Demande> findByCreateur_Id(Long id);
    List<Demande> findByEtat(Etat etat);
   List<Demande> findByDate(Date date);
    List<Demande> findByDateAndTime(Date date, Time time);
    List<Demande> findByCreateur_IdAndEtat(Long id, Etat etat);

    List<Demande> findByCreateur_IdAndDate(Long id, Date date);
    List<Demande> findByDateAndEtat(Date date, Etat etat);
    Demande findDemandeByIdDemande(Long id);
    @Modifying
    @Query("UPDATE Demande d SET d.etat = :etat WHERE d.idDemande = :id")
    void updateEtatById(@Param("id") Long id, @Param("etat") Etat etat);
    boolean existsDemandeByIdDemande(Long id);
    @Query("SELECT d FROM Demande d JOIN d.ligneDmandes l WHERE l.destinataire.id = :id")
    List<Demande> findDemandesByDestinataireId(@Param("id") Long id);


}
