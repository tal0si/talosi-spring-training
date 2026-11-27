package com.talosi.superfight.arena;

import java.util.List;

public final class ArenaDtos {
    private ArenaDtos() {}

    public record Register(String alias, String nom, String prenom, int power, String callbackUrl) {}
    public record Registered(String token, String arenaCallToken) {}
    public record PingResult(boolean reachable, Integer httpStatus, Long latencyMs, String error) {}
    public record Fighter(String participantId, String alias, int power, int hp, int maxHp, int charge, int baseDamage) {}
    public record History(int round, String yourAction, String opponentAction, String yourDefaultReason,
                          String opponentDefaultReason, int damageDealt, int damageTaken, int yourHp,
                          int opponentHp, int yourCharge, int opponentCharge) {}
    public record Turn(String turnId, String fightId, int round, int maxRounds, String deadline, int timeoutMs,
                       Fighter you, Fighter opponent, List<History> history) {}
    public record Answer(String action, String turnId) {}
}
