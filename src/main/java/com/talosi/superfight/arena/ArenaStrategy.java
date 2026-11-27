package com.talosi.superfight.arena;

import org.springframework.stereotype.Service;

import com.talosi.superfight.arena.ArenaDtos.Turn;

@Service
public class ArenaStrategy {
    public String choose(Turn turn) {
        // Deux bots identiques adoptent des rôles opposés, choisis pour chaque combat.
        boolean first = turn.you().participantId().compareTo(turn.opponent().participantId()) < 0;
        boolean defensive = first == ((turn.fightId().hashCode() & 1) == 0);
        if (defensive && (turn.round() % 3 == 1
                || (turn.opponent().charge() >= 2 && turn.round() % 2 == 1))) {
            return "GUARD";
        }
        return "ATTACK";
    }
}
