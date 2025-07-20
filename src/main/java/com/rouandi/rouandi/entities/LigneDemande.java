package com.rouandi.rouandi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Demande demande;
    @ManyToOne
    @JsonBackReference
    private Materiel materiel;
    private String ficheTechnique;
    private int quantite;
    @Enumerated(value = EnumType.STRING)
    @ManyToOne
    private Destinataire destinataire;
    private String description;

}
