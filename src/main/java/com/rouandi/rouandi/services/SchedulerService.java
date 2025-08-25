package com.rouandi.rouandi.services;

import com.rouandi.rouandi.services.Commande.Demande.DemandeServiceImpl;
import com.rouandi.rouandi.services.Commande.LigneDemande.LigneDemandeService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Configuration
@EnableScheduling
@AllArgsConstructor
public class SchedulerService {

    private final DemandeServiceImpl demandeService;
    private final LigneDemandeService ligneDemandeService;

    @Scheduled(cron = "0 0 0 * * ?")

    //@Scheduled(cron = "0/10 * * * * ?") // toutes les 10 secondes@Scheduled(fixedRate = 3000)
    public void updateDureeDemande() {
        demandeService.actualiserDureeDemandes();
        System.out.println("✔️ Tâche updateDureeDemande exécutée");
    }
    @Scheduled(cron = "0 0 0 * * ?")
   // @Scheduled(cron = "0/10 * * * * ?") // toutes les 10 secondes@Scheduled(fixedRate = 3000)
    public void updateDureeSi() {
        ligneDemandeService.incrementerDureeParEtat();

    }
}

