package com.rouandi.rouandi.DTOs.DestinataireDTO;

import com.rouandi.rouandi.DTOs.LigneDemandeDTO.LigneDemandeRequestDTO;
import com.rouandi.rouandi.entities.Destinataire;
import com.rouandi.rouandi.entities.Type;

public class DestinataireMapper {
    public static Destinataire toEntityFromDTO(LigneDemandeRequestDTO dto) {
        Destinataire d = new Destinataire();
        d.setNom(dto.getNomDestinataire());
        d.setPrenom(dto.getPrenomDestinataire());
        d.setEmail(dto.getEmailDestinataire());
        d.setProjet(dto.getProjetDestinataire());
        d.setType(Type.valueOf(dto.getTypeDestinataire().toUpperCase()));
        return d;
    }

}
