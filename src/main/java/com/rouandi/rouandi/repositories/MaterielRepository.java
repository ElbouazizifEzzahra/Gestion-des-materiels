package com.rouandi.rouandi.repositories;

import com.rouandi.rouandi.entities.Materiel;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
@Transactional
@Repository
public interface MaterielRepository extends JpaRepository<Materiel,Long> {
    @Query("SELECT m FROM Materiel m WHERE m.idMateriel = :id")
    Materiel findByIdMateriel(@Param("id") Long id);

 List<Materiel> findByNomMaterielStartingWithIgnoreCase(String prefix);
    List<Materiel> findMaterielByNomMaterielAndTypeMateriel(String nom,String type );
    void deleteMaterielByIdMateriel(Long id);
    boolean existsMaterielByIdMateriel(Long id);
    Optional<Materiel> findByNomMaterielAndTypeMateriel(String nom, String type);
    Optional<Materiel> findByNomMateriel(String nom);
    Optional<Materiel> findById(Long idMateriel);
   List<Materiel>findMaterielByNomMateriel(String nom);
 List<Materiel> findMaterielByTypeMateriel(String type);
    @Modifying
    @Query("UPDATE Materiel m SET m.typeMateriel =:type WHERE m.idMateriel = :id")
    void updateMaterielByIdAndTypeMateriel(@Param("id") Long id,@Param ("type")String type);

   @Modifying
   @Query("UPDATE Materiel m SET m.nomMateriel =:nom WHERE m.idMateriel = :id")
   void updateMaterielByIdAndNomMateriel(@Param("id") Long id,@Param ("nom")String nom);
   @Modifying
   @Query("UPDATE Materiel m SET m.nomMateriel = :nom, m.typeMateriel =:type WHERE m.idMateriel = :id")
    void updateMaterielByIdAndNomMaterielAndTypeMateriel(@Param("id") Long id,@Param ("nom")String nom, @Param ("type")String type);
    Materiel findMaterielByIdMateriel(Long id);



}
