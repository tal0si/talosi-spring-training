package com.talosi.superfight.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.service.PlayerService;

@Component
@Profile("arena-demo")
public class ArenaDemoPlayer implements CommandLineRunner {
    private final PlayerService players;
    private final String alias;
    private final int power;

    public ArenaDemoPlayer(PlayerService players, @Value("${arena.player-alias}") String alias,
                           @Value("${arena.player-power}") int power) {
        this.players = players;
        this.alias = alias;
        this.power = power;
    }

    @Override
    public void run(String... args) {
        if (power < 1 || power > 100) {
            throw new IllegalArgumentException("arena.player-power doit être compris entre 1 et 100");
        }
        players.create(PlayerDto.builder().alias(alias).name("Bot").lastname("Demo").power(power).build());
    }
}
