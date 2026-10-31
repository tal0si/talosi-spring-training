package com.talosi.superfight.controller;

import com.talosi.superfight.model.Player;
import com.talosi.superfight.service.PlayerService;
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
    public Collection<Player> findAll() {
        return playerService.getAll();
    }

    @GetMapping("/{id}")
    public Player getOne(@PathVariable("id") int id) {
        return playerService.get(id);
    }

    @PostMapping
    public Player create(@RequestBody Player player) {
        return playerService.create(player);
    }

    @PutMapping("/{id}")
    public Player update(@PathVariable("id") int id, @RequestBody Player player) {
        return playerService.update(id, player);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        playerService.delete(id);
        return ResponseEntity.ok("player well deleted");
    }

}
