package com.talosi.superfight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArenaHistory {
    private int round;
    private String yourAction;
    private String opponentAction;
    private String yourDefaultReason;
    private String opponentDefaultReason;
    private int damageDealt;
    private int damageTaken;
    private int yourHp;
    private int opponentHp;
    private int yourCharge;
    private int opponentCharge;
}
