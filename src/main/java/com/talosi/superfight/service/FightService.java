package com.talosi.superfight.service;

import com.talosi.superfight.dto.FigthDto;
import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.dto.TeamDto;
import com.talosi.superfight.model.PlayerEntity;
import com.talosi.superfight.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class FightService {
    private TeamService teamService;

    public FigthDto fight(FigthDto figth) {

        String winner = figth.getTeams().stream()
                .map(teamService::getByName)
                .map(team -> {
                    double power = team.getTeamMembers().stream()
                            .map(player -> player.getPower() * (1 + Math.random()))
                            .reduce(0.0, Double::sum);
                    team.setPower(power);
                    return team;
                })
                .sorted((p1, p2) -> Double.compare(p2.getPower(), p1.getPower()))
                .findFirst()
                .map(TeamDto::getName)
                .orElse(null);

        figth.setWinner(winner);
        return figth;
    }
}
