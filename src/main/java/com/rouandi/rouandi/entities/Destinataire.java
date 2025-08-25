package com.rouandi.rouandi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Destinataire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String fonction;
    private String projet;
    @Enumerated(value= EnumType.STRING)
    private Type type;
    @OneToMany(mappedBy = "destinataire", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LigneDemande> lignesDemande;


}
