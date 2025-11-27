package com.example.api.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String username;
    private String password;


}

//LoginRequestDTO sert seulement à recevoir les données du client (ce que l’utilisateur envoie, ici username et password)