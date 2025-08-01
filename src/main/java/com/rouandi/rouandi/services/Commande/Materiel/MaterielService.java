package com.rouandi.rouandi.services.Commande.Materiel;

import com.rouandi.rouandi.entities.Materiel;

public interface MaterielService {
    public Materiel ajouterMateriel(Materiel materiel);
    public void deleteMateriel(Long id);
    Materiel  updateMateriel(Long id,String type,String nom);
}
