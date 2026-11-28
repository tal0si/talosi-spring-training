package com.talosi.superfight.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArenaTurn {
    private String turnId;
    private String fightId;
    private int round;
    private int maxRounds;
    private String deadline;
    private int timeoutMs;
    private ArenaFighter you;
    private ArenaFighter opponent;
    private List<ArenaHistory> history;
}
