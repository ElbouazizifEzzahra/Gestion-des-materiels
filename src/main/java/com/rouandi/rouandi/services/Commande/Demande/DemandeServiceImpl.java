package com.rouandi.rouandi.services.Commande.Demande;

import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Etat;;
import com.rouandi.rouandi.repositories.DemandeRepository;
import com.rouandi.rouandi.services.Commande.User.EmailService;
import com.rouandi.rouandi.services.Commande.User.UserServiceImp;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.sql.Time;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


@AllArgsConstructor
@Transactional
@Service
public class DemandeServiceImpl implements DemandeService {


    private DemandeRepository demandeRepository;
    private UserServiceImp userServiceImp;
    private EmailService emailService;




    @Override
    public Demande addDemande(Demande demande) {
                demande.setTime(new Time(System.currentTimeMillis()));
                demande.setDate(new Date());
                demande.setCreateur(userServiceImp.getCurrentUser());
                demande.setDuree(0L);
        emailService.sendEmail(
                demande.getCreateur().getEmail(),
                "elbouazizi.fat.fst@uhp.ac.ma",
                "Ajout d'une nouvelle demande ",
                " Nouvelle demande – Visitez la plateforme pour le suivi. "
        );
        return demandeRepository.save(demande);
    }

    @Override
    public void delete(Long id) {
        if (demandeRepository.existsById(id))
            demandeRepository.deleteDemandeByIdDemande(id);
        else
            System.out.println("la ligne n'existe pas");
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
                    "elbouazizi.fat.fst@uhp.ac.ma",
                    emailCreateur,
                    "Changement d'état de votre demande",
                    "L'état actuel de votre demande est : " + etat
            );
        }
    }
 /*   @Override
    public void actualiserDureeDemandes() {
        List<Demande> demandes = demandeRepository.findAll();
        Date today = new Date();

        for (Demande d : demandes) {
            if (!d.getEtat().equals(Etat.TRAITE)) {
                long diffInMillies = today.getTime() - d.getDate().getTime();
                long diffJours = diffInMillies / (24 * 60 * 60 * 1000);
                d.setDuree(diffJours);
                demandeRepository.save(d);
            }
        }
    }*/
    @Override
    @Transactional
    public void actualiserDureeDemandes() {
        Date today = new Date();
        List<Demande> demandes = demandeRepository.findAll();

        demandes.forEach(d -> {
            if (d.getDate() != null && !Etat.TRAITE.equals(d.getEtat())) {
                Long current = d.getDuree() != null ? d.getDuree() : 0L;
                d.setDuree(current + 1);
            }
        });

        demandeRepository.saveAll(demandes);
    }

}
