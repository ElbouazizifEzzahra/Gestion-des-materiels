package com.rouandi.rouandi.services.Commande.Demande;

import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Etat;
import com.rouandi.rouandi.entities.User;
import com.rouandi.rouandi.repositories.DemandeRepository;
import com.rouandi.rouandi.services.Commande.Demande.DemandeService;
import com.rouandi.rouandi.services.Commande.User.EmailService;
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
    private EmailService emailService;

    public DemandeServiceImpl(DemandeRepository demandeRepository, UserServiceImp userServiceImp,EmailService emailService) {
        this.demandeRepository = demandeRepository;
        this.userServiceImp = userServiceImp;
        this.emailService=emailService;
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
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));


        demande.setEtat(etat);
        demandeRepository.updateEtatById(id, etat);


        String emailCreateur = demande.getCreateur().getEmail();
        if (emailCreateur != null && !emailCreateur.isEmpty()) {
            emailService.sendEmail(
                    "elbouazizi.fat.fst@uhp.ac.ma", // adresse de l'expéditeur
                    emailCreateur,
                    "Changement d'état de votre demande",
                    "L'état actuel de votre demande est : " + etat
            );
        }
    }

}
