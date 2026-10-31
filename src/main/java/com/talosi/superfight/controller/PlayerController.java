package com.talosi.superfight.controller;

import com.talosi.superfight.model.Player;
import com.talosi.superfight.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

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
    public Collection<Player> findAll() {
        return playerService.getAll();
    }

    @GetMapping("/{id}")
    @Operation(description = "get on player by id")
    public Player getOne(@PathVariable("id") int id) {
        return playerService.get(id);
    }

    @PostMapping
    @Operation(description = "create a player")
    public Player create(@RequestBody Player player) {
        return playerService.create(player);
    }

    @PutMapping("/{id}")
    @Operation(description = "update a player")
    public Player update(@PathVariable("id") int id, @RequestBody Player player) {
        return playerService.update(id, player);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "delete a player")
    public ResponseEntity delete(@PathVariable("id") int id) {
        playerService.delete(id);
        return ResponseEntity.ok("player well deleted");
    }

}
