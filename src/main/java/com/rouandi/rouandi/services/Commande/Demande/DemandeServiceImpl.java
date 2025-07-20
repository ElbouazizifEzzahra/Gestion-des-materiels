package com.rouandi.rouandi.services.Commande.Demande;

import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Etat;
import com.rouandi.rouandi.repositories.DemandeRepository;
import com.rouandi.rouandi.services.Commande.Demande.DemandeService;
import com.rouandi.rouandi.services.Commande.User.UserServiceImp;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;

@Transactional
@Service
public class DemandeServiceImpl implements DemandeService {


    private DemandeRepository demandeRepository;
    private UserServiceImp userServiceImp;


    public DemandeServiceImpl(DemandeRepository demandeRepository, UserServiceImp userServiceImp) {
        this.demandeRepository = demandeRepository;
        this.userServiceImp = userServiceImp;
    }

    @Override
    public Demande addDemande(Demande demande) {
                demande.setTime(new Time(System.currentTimeMillis()));
                demande.setDate(new Date());
                demande.setCreateur(userServiceImp.getCurrentUser());
        return demandeRepository.save(demande);
    }

    @Override
    public void update(Long id, Etat etat) {
        demandeRepository.updateEtatById(id,etat);
    }


}
