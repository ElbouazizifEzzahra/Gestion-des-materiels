package com.rouandi.rouandi.repositories;

import com.rouandi.rouandi.entities.Destinataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DestinataireRepository  extends JpaRepository<Destinataire,Long> {
    Optional<Destinataire> findDestinataireByEmail(String email);
    void deleteDestinataireById(Long id);
    boolean existsDestinataireByEmail(String email);

}
