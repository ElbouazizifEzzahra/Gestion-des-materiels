package com.rouandi.rouandi.DTOs.UserDTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String nom;
    private String prenom;
    private String fonctionalite;
    private String numeroTelephone;
    private String email;
    private String code;
    private String societe;
    private String site;
    private String password;
    private String role;
    private String projet;
}
