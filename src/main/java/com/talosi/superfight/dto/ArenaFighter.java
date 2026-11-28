package com.talosi.superfight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArenaFighter {
    private String participantId;
    private String alias;
    private int power;
    private int hp;
    private int maxHp;
    private int charge;
    private int baseDamage;
}
