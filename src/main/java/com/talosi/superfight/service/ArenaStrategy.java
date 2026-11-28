package com.talosi.superfight.service;

import org.springframework.stereotype.Service;

import com.talosi.superfight.dto.ArenaTurn;

@Service
public class ArenaStrategy {
    public String choose(ArenaTurn turn) {
        // Deux bots identiques adoptent des rôles opposés, choisis pour chaque combat.
        boolean first = turn.getYou().getParticipantId().compareTo(turn.getOpponent().getParticipantId()) < 0;
        boolean defensive = first == ((turn.getFightId().hashCode() & 1) == 0);
        if (defensive && (turn.getRound() % 3 == 1
                || (turn.getOpponent().getCharge() >= 2 && turn.getRound() % 2 == 1))) {
            return "GUARD";
        }
        return "ATTACK";
    }
}
