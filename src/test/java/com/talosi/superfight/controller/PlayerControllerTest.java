package com.talosi.superfight.controller;

import com.talosi.superfight.model.Player;
import com.talosi.superfight.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(PlayerController.class)
@Import({PlayerService.class, RestResponseEntityExceptionHandler.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PlayerControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PlayerService playerService;

    @Test
    void missingPlayerReturns404ForGetAndDelete() throws Exception {
        mvc.perform(get("/players/999")).andExpect(status().isNotFound());
        mvc.perform(delete("/players/999")).andExpect(status().isNotFound());
        mvc.perform(put("/players/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerJson(999, "Missing")))
                .andExpect(status().isNotFound());
    }

    @Test
    void existingPlayerCanBeRetrievedAndDeleted() throws Exception {
        playerService.create(new Player(1, "First", null, null, null, null, 0));
        mvc.perform(get("/players/1")).andExpect(status().isOk());
        mvc.perform(delete("/players/1")).andExpect(status().isOk());
        mvc.perform(get("/players/1")).andExpect(status().isNotFound());
    }

    @Test
    void duplicateIdReturns409WithoutReplacingExistingPlayer() throws Exception {
        createPlayer(1, "First");
        mvc.perform(post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerJson(1, "Duplicate")))
                .andExpect(status().isConflict());
        mvc.perform(get("/players/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"First\"")));
    }

    @Test
    void updateUsesRouteIdAndDoesNotOverwriteBodyIdPlayer() throws Exception {
        createPlayer(1, "First");
        createPlayer(2, "Second");

        mvc.perform(put("/players/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerJson(2, "Updated")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"id\":1")))
                .andExpect(content().string(containsString("\"name\":\"Updated\"")));
        mvc.perform(get("/players/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"Updated\"")));
        mvc.perform(get("/players/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"Second\"")));
    }

    @Test
    void updateCannotCreateAnAbsentRouteIdEvenWhenBodyIdExists() throws Exception {
        createPlayer(2, "Second");
        mvc.perform(put("/players/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerJson(2, "Changed")))
                .andExpect(status().isNotFound());
        mvc.perform(get("/players/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"name\":\"Second\"")));
        mvc.perform(get("/players/999")).andExpect(status().isNotFound());
    }

    private void createPlayer(int id, String name) throws Exception {
        mvc.perform(post("/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(playerJson(id, name)))
                .andExpect(status().isOk());
    }

    private String playerJson(int id, String name) {
        return "{\"id\":" + id + ",\"name\":\"" + name + "\",\"lastname\":\"Last\","
                + "\"alias\":\"Hero\",\"genre\":\"MALE\",\"dimension\":\"marvel\",\"power\":10}";
    }
}
