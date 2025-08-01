package com.rouandi.rouandi.DTOs.UserDTO;

import com.rouandi.rouandi.entities.Materiel;
import com.rouandi.rouandi.entities.Role;
import com.rouandi.rouandi.entities.User;

public class UserMapper {
    public static UserResponseDTO toDTO(User user){
        UserResponseDTO userResponseDTO=new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setFonctionalite(user.getFonctionalite());
        userResponseDTO.setNom(user.getNom());
        userResponseDTO.setNumeroTelephone(user.getNumeroTelephone());
        userResponseDTO.setPrenom(user.getPrenom());
        if (user.getRole() != null) {
            userResponseDTO.setRole(user.getRole().name());
        }
        userResponseDTO.setCode(user.getCode());
        userResponseDTO.setProjet(user.getProjet());
        userResponseDTO.setSite(user.getSite());
        userResponseDTO.setSociete(user.getSociete());

        return userResponseDTO;
    }
    public static User fromDTO(UserRequestDTO userRequestDTO){
        User user=new User();
        user.setEmail(userRequestDTO.getEmail());
        user.setFonctionalite(userRequestDTO.getFonctionalite());
        user.setNom(userRequestDTO.getNom());
        user.setNumeroTelephone(userRequestDTO.getNumeroTelephone());
        user.setPrenom(userRequestDTO.getPrenom());
        user.setPassword(userRequestDTO.getPassword());
        if (userRequestDTO.getRole() != null) {
            try {
                user.setRole(Role.valueOf(userRequestDTO.getRole().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Rôle invalide : " + userRequestDTO.getRole());
            }
        }
        user.setCode(userRequestDTO.getCode());
        user.setProjet(userRequestDTO.getProjet());
        user.setSite(userRequestDTO.getSite());
        user.setSociete(userRequestDTO.getSociete());

        return user;
    }

}
