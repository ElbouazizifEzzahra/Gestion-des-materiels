package com.rouandi.rouandi.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.util.*;

@Entity
@NoArgsConstructor
@Data
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long idDemande ;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Africa/Casablanca")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private Time time;
    @Enumerated(value = EnumType.STRING)
    private Etat etat ;

    @OneToMany(mappedBy = "demande",cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<LigneDemande> ligneDemandes = new ArrayList<>();
    @ManyToOne
    private User createur;

    private Long duree;


}
