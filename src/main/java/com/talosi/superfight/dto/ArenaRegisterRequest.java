package com.talosi.superfight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArenaRegisterRequest {
    private String alias;
    private String nom;
    private String prenom;
    private int power;
    private String callbackUrl;
}
