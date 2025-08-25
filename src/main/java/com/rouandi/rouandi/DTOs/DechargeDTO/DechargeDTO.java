package com.rouandi.rouandi.DTOs.DechargeDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class DechargeDTO {
    private String nomDestinataire;
    private String prenomDestinataire;
    private Date date;
    private String lieu ;
    private String[]caracteristiques;
    private String nomMateriel;
    private String typeMateriel;

}
