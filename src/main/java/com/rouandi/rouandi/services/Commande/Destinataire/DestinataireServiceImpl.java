package com.rouandi.rouandi.services.Commande.Destinataire;

import com.rouandi.rouandi.entities.Destinataire;
import com.rouandi.rouandi.repositories.DestinataireRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DestinataireServiceImpl implements DestinataireService{
    private DestinataireRepository destinataireRepository;
    @Override
    public void supprimerDestinataire(Destinataire destinataire) {
        if(destinataireRepository.existsDestinataireByEmail(destinataire.getEmail()))
            destinataireRepository.deleteDestinataireById(destinataire.getId());
    }
}
