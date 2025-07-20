package com.rouandi.rouandi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Materiel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMateriel;
    private String typeMateriel;
    private String nomMateriel;
    @OneToMany(mappedBy = "materiel", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<LigneDemande> ligneDmandes = new ArrayList<>();

}
