package com.rouandi.rouandi.services.Query.Materiel;

import com.rouandi.rouandi.entities.Materiel;

import java.util.List;

public interface MaterielQueryService {
    public List<Materiel> filter(String nom, String type);
}
