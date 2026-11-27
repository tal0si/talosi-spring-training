package com.talosi.superfight.arena;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.talosi.superfight.service.PlayerService;

@SpringBootTest(properties = {"arena.enabled=false", "apikey=arena-test-key"})
@ActiveProfiles("arena-demo")
@AutoConfigureMockMvc
class ArenaControllerIntegrationTest {
    private static final String TURN = """
            {"turnId":"t1","fightId":"f1","round":1,"maxRounds":30,
             "deadline":"2026-01-01T00:00:00Z","timeoutMs":2000,
             "you":{"participantId":"a","alias":"A","power":60,"hp":140,"maxHp":140,"charge":2,"baseDamage":17},
             "opponent":{"participantId":"b","alias":"B","power":50,"hp":150,"maxHp":150,"charge":0,"baseDamage":15},
             "history":[]}
            """;

    @Autowired private MockMvc mvc;
    @Autowired private ArenaCredentials credentials;
    @Autowired private PlayerService players;

    @BeforeEach
    void setUp() {
        credentials.install("callback-secret");
    }

    @Test
    void demoPlayerExistsInDatabase() {
        assertThat(players.getByAlias("ArenaBot").getPower()).isEqualTo(60);
    }

    @Test
    void callbacksRequireArenaTokenAndReturnStrategyAction() throws Exception {
        mvc.perform(get("/arena/ping")).andExpect(status().isUnauthorized());
        mvc.perform(get("/arena/ping").header("X-Arena-Token", "wrong"))
                .andExpect(status().isUnauthorized());
        mvc.perform(get("/arena/ping").header("X-Arena-Token", "callback-secret"))
                .andExpect(status().isOk());
        mvc.perform(post("/arena/turn").contentType(MediaType.APPLICATION_JSON).content(TURN))
                .andExpect(status().isUnauthorized());
        mvc.perform(post("/arena/turn").header("X-Arena-Token", "callback-secret")
                .header("X-Arena-Turn-Id", "other").contentType(MediaType.APPLICATION_JSON).content(TURN))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/arena/turn").header("X-Arena-Token", "callback-secret")
                .header("X-Arena-Turn-Id", "t1").contentType(MediaType.APPLICATION_JSON).content(TURN))
                .andExpect(status().isOk()).andExpect(jsonPath("$.action").value("ATTACK"))
                .andExpect(jsonPath("$.turnId").value("t1"));
    }

    @Test
    void otherWritesStillRequireApiKey() throws Exception {
        mvc.perform(post("/players").contentType(MediaType.APPLICATION_JSON)
                .content("{\"alias\":\"Other\",\"power\":50}")).andExpect(status().isForbidden());
    }
}
