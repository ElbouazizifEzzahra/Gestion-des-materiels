package com.rouandi.rouandi.services.Commande.Demande;

import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Etat;
import com.rouandi.rouandi.repositories.DemandeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Transactional
@Service
public interface DemandeService {

    public Demande addDemande(Demande demande);
    public void delete(Long id);
    void update(Long id, Etat etat);
    public void actualiserDureeDemandes();

}
