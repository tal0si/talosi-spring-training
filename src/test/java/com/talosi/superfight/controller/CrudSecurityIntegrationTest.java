package com.talosi.superfight.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "apikey=crud-test-key")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CrudSecurityIntegrationTest {

    @Autowired private MockMvc mvc;

    @Test
    void filtersProtectWritesAndAllowAuthenticatedRequestsToReach404() throws Exception {
        UUID id = UUID.randomUUID();
        String player = "{\"alias\":\"absent\",\"power\":0}";
        String team = "{\"name\":\"absent\",\"teamMembers\":[]}";
        mvc.perform(get("/players/{id}", id)).andExpect(status().isNotFound());
        mvc.perform(put("/players/{id}", id).contentType(MediaType.APPLICATION_JSON).content(player))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/players/{id}", id)).andExpect(status().isForbidden());
        mvc.perform(put("/teams/{id}", id).contentType(MediaType.APPLICATION_JSON).content(team))
                .andExpect(status().isForbidden());
        mvc.perform(delete("/teams/{id}", id)).andExpect(status().isForbidden());
        mvc.perform(put("/players/{id}", id).header("x-api-key", "crud-test-key")
                .contentType(MediaType.APPLICATION_JSON).content(player)).andExpect(status().isNotFound());
        mvc.perform(delete("/players/{id}", id).header("x-api-key", "crud-test-key"))
                .andExpect(status().isNotFound());
        mvc.perform(put("/teams/{id}", id).header("x-api-key", "crud-test-key")
                .contentType(MediaType.APPLICATION_JSON).content(team)).andExpect(status().isNotFound());
        mvc.perform(delete("/teams/{id}", id).header("x-api-key", "crud-test-key"))
                .andExpect(status().isNotFound());
    }
}
