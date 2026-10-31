package com.talosi.superfight.service;

import com.talosi.superfight.dto.TeamDto;
import com.talosi.superfight.exception.HttpRequestException;
import com.talosi.superfight.exception.NotFoundException;
import com.talosi.superfight.mapper.TeamMapper;
import com.talosi.superfight.model.TeamEntity;
import com.talosi.superfight.repository.PlayerRepository;
import com.talosi.superfight.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TeamService {

    private TeamRepository teamRepository;
    private PlayerRepository playerRepository;
    private TeamMapper teamMapper;

    public List<TeamDto> getAll() {
        return teamMapper.mapToDto(teamRepository.findAll());
    }

    public TeamDto create(TeamDto team) {
        if(teamRepository.findByName(team.getName()) != null) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "Team " + team.getName() + " already exists");
        }

        TeamEntity teamEntity = teamMapper.map(team);

        teamEntity.setTeamMembers(
                teamEntity.getTeamMembers().stream().map((member ->
                playerRepository
                        .findByAlias(member.getAlias())
                        .orElseThrow(() -> new NotFoundException("No Player found with id " + member.getId()))
        )).toList());

        return teamMapper.map(teamRepository.save(teamEntity));
    }

    public TeamDto get(UUID id) {
        return teamMapper.map(
                teamRepository
                        .findById(id)
                        .orElseThrow(() -> new NotFoundException("No Team found with id " + id))
        );
    }

    public void delete(UUID id) {
        this.get(id); // to check if exists
        teamRepository.deleteById(id);
    }

    public TeamDto update(UUID id, TeamDto team) {
        this.get(id); // to check if exists
        team.setId(id);

        return teamMapper.map(teamRepository.save(
                teamMapper.map(team)
        ));
    }

}
