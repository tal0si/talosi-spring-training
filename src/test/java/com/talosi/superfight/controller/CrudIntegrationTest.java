package com.talosi.superfight.controller;

import com.talosi.superfight.model.PlayerEntity;
import com.talosi.superfight.model.TeamEntity;
import com.talosi.superfight.repository.PlayerRepository;
import com.talosi.superfight.repository.TeamRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "apikey=crud-test-key")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CrudIntegrationTest {

    @Autowired private MockMvc mvc;
    @Autowired private PlayerRepository players;
    @Autowired private TeamRepository teams;
    @Autowired private TransactionTemplate transactions;

    @Test
    void missingPlayersReturn404ForGetUpdateAndDelete() throws Exception {
        UUID id = UUID.randomUUID();
        mvc.perform(get("/players/{id}", id)).andExpect(status().isNotFound());
        mvc.perform(put("/players/{id}", id).header("x-api-key", "crud-test-key")
                .contentType(MediaType.APPLICATION_JSON).content("{\"alias\":\"absent\",\"power\":0}"))
                .andExpect(status().isNotFound());
        mvc.perform(delete("/players/{id}", id).header("x-api-key", "crud-test-key"))
                .andExpect(status().isNotFound());
        assertThat(players.existsById(id)).isFalse();
    }

    @Test
    void existingPlayerCanBeUpdatedAndDeleted() throws Exception {
        PlayerEntity player = new PlayerEntity();
        player.setAlias("player-" + UUID.randomUUID());
        player = players.saveAndFlush(player);
        UUID id = player.getId();
        String alias = player.getAlias();

        mvc.perform(put("/players/{id}", id).header("x-api-key", "crud-test-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"alias\":\"" + alias + "\",\"name\":\"Updated\",\"power\":0}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Updated"));
        mvc.perform(delete("/players/{id}", id).header("x-api-key", "crud-test-key"))
                .andExpect(status().isOk());
        mvc.perform(get("/players/{id}", id)).andExpect(status().isNotFound());
    }

    @Test
    void teamUpdateResolvesAliasesAndPreservesPlayerData() throws Exception {
        PlayerEntity first = new PlayerEntity();
        first.setAlias("first-" + UUID.randomUUID());
        first.setName("Original first");
        first = players.saveAndFlush(first);
        PlayerEntity second = new PlayerEntity();
        second.setAlias("second-" + UUID.randomUUID());
        second.setName("Original second");
        second = players.saveAndFlush(second);

        UUID firstId = first.getId();
        UUID id = transactions.execute(status -> {
            TeamEntity team = new TeamEntity();
            team.setName("Before");
            team.getTeamMembers().add(players.findById(firstId).orElseThrow());
            return teams.saveAndFlush(team).getId();
        });
        String body = "{\"name\":\"After\",\"teamMembers\":[{\"alias\":\"" + first.getAlias()
                + "\",\"power\":0},{\"alias\":\"" + second.getAlias() + "\",\"power\":0}]}";

        mvc.perform(put("/teams/{id}", id).header("x-api-key", "crud-test-key")
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("After"))
                .andExpect(jsonPath("$.teamMembers.length()").value(2));
        mvc.perform(get("/teams/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.teamMembers[*].id",
                        containsInAnyOrder(first.getId().toString(), second.getId().toString())));
        assertThat(players.findById(first.getId()).orElseThrow().getName()).isEqualTo("Original first");
        assertThat(players.findById(second.getId()).orElseThrow().getName()).isEqualTo("Original second");

        mvc.perform(put("/teams/{id}", id).header("x-api-key", "crud-test-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"After\",\"teamMembers\":[{\"alias\":\"" + second.getAlias() + "\",\"power\":0}]}"))
                .andExpect(status().isOk());
        mvc.perform(get("/teams/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.teamMembers[*].id", containsInAnyOrder(second.getId().toString())));
        assertThat(players.findById(first.getId()).orElseThrow().getName()).isEqualTo("Original first");

        mvc.perform(put("/teams/{id}", id).header("x-api-key", "crud-test-key")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Invalid\",\"teamMembers\":[{\"alias\":\"missing-" + UUID.randomUUID() + "\",\"power\":0}]}"))
                .andExpect(status().isNotFound());
        mvc.perform(get("/teams/{id}", id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("After"))
                .andExpect(jsonPath("$.teamMembers.length()").value(1));
    }

    @Test
    void teamEndpointsHandleExistingAndMissingIds() throws Exception {
        UUID absent = UUID.randomUUID();
        String body = "{\"name\":\"Team\",\"teamMembers\":[]}";
        mvc.perform(put("/teams/{id}", absent).header("x-api-key", "crud-test-key")
                .contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound());
        mvc.perform(delete("/teams/{id}", absent).header("x-api-key", "crud-test-key"))
                .andExpect(status().isNotFound());

        TeamEntity team = new TeamEntity();
        team.setName("Empty");
        team = teams.saveAndFlush(team);
        mvc.perform(delete("/teams/{id}", team.getId()).header("x-api-key", "crud-test-key"))
                .andExpect(status().isOk());
        mvc.perform(get("/teams/{id}", team.getId())).andExpect(status().isNotFound());
    }
}
