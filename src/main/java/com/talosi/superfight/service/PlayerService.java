package com.talosi.superfight.service;

import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.exception.HttpRequestException;
import com.talosi.superfight.mapper.PlayerMapper;
import com.talosi.superfight.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class PlayerService {
    private PlayerRepository playerRepository;
    private PlayerMapper playerMapper;

    public Collection<PlayerDto> getAll() {
        return playerMapper.mapToDto(playerRepository.findAll());
    }

    public PlayerDto get(UUID id) {
        return
                playerMapper.map(playerRepository.findById(id)
                .orElse(null)
                );
    }

    public PlayerDto getByAlias(String alias) {
        return
                playerMapper.map(
                        playerRepository.findByAlias(alias)
                                        .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Player not found"))
                );
    }

    public PlayerDto create(PlayerDto player) {
        if(playerRepository.findByAlias(player.getAlias()).isPresent()) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "Player already exists");
        }
        return playerMapper.map(playerRepository.save(
                playerMapper.map(player))
        );
    }

    public void delete(UUID id) {
        this.get(id); // to check if exists
        playerRepository.deleteById(id);
    }

    public PlayerDto update(UUID id, PlayerDto player) {
        this.get(id); // to check if exists
        player.setId(id);

        return playerMapper.map(playerRepository.save(
                playerMapper.map(player)
        ));
    }
}
