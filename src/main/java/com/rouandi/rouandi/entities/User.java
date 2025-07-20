package com.rouandi.rouandi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String nom;
    private String prenom;
    private String NumeroTelephone;
    private String fonctionalite;
    private String code;
    private String societe;
    private String projet ;
    private String site;
    @Enumerated(value = EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "createur",cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Demande>demandes=new ArrayList<>();

}
