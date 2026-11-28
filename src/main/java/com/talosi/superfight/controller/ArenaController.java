package com.talosi.superfight.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talosi.superfight.dto.ArenaAnswer;
import com.talosi.superfight.dto.ArenaTurn;
import com.talosi.superfight.service.ArenaStrategy;

@RestController
@RequestMapping("/arena")
public class ArenaController {
    private final ArenaStrategy strategy;

    public ArenaController(ArenaStrategy strategy) {
        this.strategy = strategy;
    }

    @GetMapping("/ping")
    public ResponseEntity<Void> ping() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/turn")
    public ResponseEntity<ArenaAnswer> turn(
            @RequestHeader(value = "X-Arena-Turn-Id", required = false) String turnId,
            @RequestBody ArenaTurn turn) {
        if (turnId == null || turnId.isBlank() || turn.getTurnId() == null
                || !turnId.equals(turn.getTurnId()) || turn.getYou() == null || turn.getOpponent() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new ArenaAnswer(strategy.choose(turn), turn.getTurnId()));
    }
}
