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
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Temporal(TemporalType.TIME)
    @JsonFormat(pattern = "HH:mm:ss")
    private Time time;
    @Enumerated(value = EnumType.STRING)
    private Etat etat ;

    @OneToMany(mappedBy = "demande",cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LigneDemande> ligneDmandes = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.REMOVE)
    private User createur;

}
