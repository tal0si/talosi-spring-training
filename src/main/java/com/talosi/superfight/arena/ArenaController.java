package com.talosi.superfight.arena;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.talosi.superfight.arena.ArenaDtos.Answer;
import com.talosi.superfight.arena.ArenaDtos.Turn;

@RestController
@RequestMapping("/arena")
public class ArenaController {
    private final ArenaCredentials credentials;
    private final ArenaStrategy strategy;

    public ArenaController(ArenaCredentials credentials, ArenaStrategy strategy) {
        this.credentials = credentials;
        this.strategy = strategy;
    }

    @GetMapping("/ping")
    public ResponseEntity<Void> ping(@RequestHeader(value = "X-Arena-Token", required = false) String token) {
        return credentials.accepts(token) ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/turn")
    public ResponseEntity<Answer> turn(
            @RequestHeader(value = "X-Arena-Token", required = false) String token,
            @RequestHeader(value = "X-Arena-Turn-Id", required = false) String turnId,
            @RequestBody Turn turn) {
        if (!credentials.accepts(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (turnId == null || turnId.isBlank() || turn.turnId() == null
                || !turnId.equals(turn.turnId()) || turn.you() == null || turn.opponent() == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(new Answer(strategy.choose(turn), turn.turnId()));
    }
}
