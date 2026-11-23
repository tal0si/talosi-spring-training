package com.talosi.superfight.controller;


import com.talosi.superfight.dto.TeamDto;
import com.talosi.superfight.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teams")
@AllArgsConstructor
public class TeamController {

    private TeamService teamService;

    @Operation(description = "Find all teams")
    @GetMapping
    public List<TeamDto> findAll() {
        return teamService.getAll();
    }

    @Operation(description = "Find one team")
    @GetMapping("/{id}")
    public TeamDto getOne(@PathVariable("id") UUID id) {
        return teamService.get(id);
    }

    @Operation(description = "create a team")
    @PostMapping
    public TeamDto create(@RequestBody TeamDto team) {
        return teamService.create(team);
    }

    @Operation(description = "update a team")
    @PutMapping("/{id}")
    public TeamDto update(@PathVariable("id") UUID id, @RequestBody TeamDto team) {
        return teamService.update(id, team);
    }

    @Operation(description = "delete a team")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id) {
        teamService.delete(id);
        return ResponseEntity.ok("team well deleted");
    }

}
