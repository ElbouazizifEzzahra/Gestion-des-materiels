package com.rouandi.rouandi.services.Query.Demande;

import com.rouandi.rouandi.entities.Demande;
import com.rouandi.rouandi.entities.Etat;
import com.rouandi.rouandi.entities.User;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface DemandeQueryService {
    List<Demande>demandesByUser(Long id);
    List <Demande>filterDemande(Etat etat, Date date, Time time,Long id);


}
