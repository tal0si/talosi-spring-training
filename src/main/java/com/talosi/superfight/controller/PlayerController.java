package com.talosi.superfight.controller;

import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/players")
@AllArgsConstructor
public class PlayerController {

    private PlayerService playerService;

    @GetMapping
    @Operation(description = "Get all players")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The Service Provider has been found."),
            @ApiResponse(responseCode = "404", description = "The Service Provider requested doesn't exists.")}
    )
    public Collection<PlayerDto> findAll() {
        return playerService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(description = "get on player by id")
    public PlayerDto getOne(@PathVariable("id") UUID id) {
        return playerService.get(id);
    }

    @PutMapping()
    @Operation(description = "create a player")
    public PlayerDto create(@Parameter(description = "player to create")  @RequestBody PlayerDto player) {
        return playerService.create(player);
    }

    @PutMapping("/{id}")
    @Operation(description = "update a player")
    public PlayerDto update(@PathVariable("id") UUID id, @RequestBody PlayerDto player) {
        return playerService.update(id, player);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "delete a player")
    public ResponseEntity delete(@PathVariable("id") UUID id) {
        playerService.delete(id);
        return ResponseEntity.ok("player well deleted");
    }

}
