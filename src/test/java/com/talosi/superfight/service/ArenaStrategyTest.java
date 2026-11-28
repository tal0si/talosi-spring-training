package com.talosi.superfight.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.talosi.superfight.configuration.ArenaCredentials;
import com.talosi.superfight.dto.ArenaFighter;
import com.talosi.superfight.dto.ArenaTurn;

class ArenaStrategyTest {
    private final ArenaStrategy strategy = new ArenaStrategy();

    private ArenaTurn turn(String fightId, int round, boolean reverse, int opponentCharge) {
        ArenaFighter self = new ArenaFighter("a", "A", 60, 140, 140, 0, 17);
        ArenaFighter opponent = new ArenaFighter("b", "B", 50, 150, 150, opponentCharge, 15);
        return new ArenaTurn("t1", fightId, round, 30, null, 2000,
                reverse ? opponent : self, reverse ? self : opponent, List.of());
    }

    @Test
    void mirrorBotsChooseComplementaryStylesAndCanSwapOnAnotherFight() {
        assertThat(strategy.choose(turn("fight0", 1, false, 0))).isEqualTo("GUARD");
        assertThat(strategy.choose(turn("fight0", 1, true, 0))).isEqualTo("ATTACK");
        assertThat(strategy.choose(turn("fight1", 1, false, 0))).isEqualTo("ATTACK");
        assertThat(strategy.choose(turn("fight1", 1, true, 0))).isEqualTo("GUARD");
    }

    @Test
    void defensiveBotAttacksAndGuardsAgainstAChargedOpponent() {
        assertThat(strategy.choose(turn("fight0", 2, false, 0))).isEqualTo("ATTACK");
        assertThat(strategy.choose(turn("fight0", 3, false, 2))).isEqualTo("GUARD");
    }

    @Test
    void refusesTokensBeforeInstallation() {
        ArenaCredentials credentials = new ArenaCredentials();
        assertThat(credentials.accepts(null)).isFalse();
        assertThat(credentials.accepts("")).isFalse();
        assertThat(credentials.accepts("arbitrary")).isFalse();
    }
}
