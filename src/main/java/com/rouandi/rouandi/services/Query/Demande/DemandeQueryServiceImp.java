package com.rouandi.rouandi.services.Query.Demande;

import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Etat;
import com.rouandi.rouandi.entities.User;
import com.rouandi.rouandi.repositories.DemandeRepository;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;
import java.util.List;
@Service
public class DemandeQueryServiceImp implements DemandeQueryService {
    private DemandeRepository demandeRepository;

    public DemandeQueryServiceImp(DemandeRepository demandeRepository) {
        this.demandeRepository = demandeRepository;
    }

    @Override
    public List<Demande> demandesByUser(Long id) {
        List<Demande> demandes = demandeRepository.findByCreateur_Id(id);
        return demandes;
    }

    @Override
    public List<Demande> filterDemande(Etat etat, Date date, Time time, Long createurId) {

        if (createurId != null && etat != null) {
            return demandeRepository.findByCreateur_IdAndEtat(createurId, etat)
                    .stream()
                    .toList();
        }
        if (createurId != null && date != null) {
            return demandeRepository.findByCreateur_IdAndDate(createurId, date);
        }

        if (createurId != null) {
            return demandeRepository.findByCreateur_Id(createurId);
        }
        if (date != null && time != null) {
            return demandeRepository.findByDateAndTime(date, time);
        }
        if (date != null && etat != null) {
            return demandeRepository.findByDateAndEtat(date, etat);
        }
        if (date != null) {
            return demandeRepository.findByDate(date);
        }
        if (etat != null) {
            return demandeRepository.findByEtat(etat);
        }
    else
        return demandeRepository.findAll();
    }

    @Override
    public List<Demande> demandes() {
        return demandeRepository.findAll();
    }


}