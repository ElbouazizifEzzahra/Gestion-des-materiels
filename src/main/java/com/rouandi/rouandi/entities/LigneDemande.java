package com.rouandi.rouandi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneDemande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    @ToString.Exclude
    private Demande demande;
    @ManyToOne
    @JsonManagedReference
    @ToString.Exclude
    private Materiel materiel;
    private String ficheTechnique;
    private int quantite;
    @Enumerated(value = EnumType.STRING)
    @ManyToOne
    private Destinataire destinataire;
    private String description;
    private String decharge;
    @Enumerated(value = EnumType.STRING)
    private EtatLigne etat ;
    @Enumerated(value = EnumType.STRING)
    private TypeLivraison typeLivraison ;
    private Long dureeSi;
    private Long dureeLogistique;
}
