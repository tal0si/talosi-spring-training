package com.talosi.superfight.controller;

import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.model.GenderEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PlayerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper objectMapper;

    @Test
    public void testGetPlayer() throws Exception{
        mockMvc.perform(get("/players")).andExpect(status().isOk());
    }

    @Test
    public void testPostPlayerWithSameAliasReturnError() throws Exception{
        PlayerDto playerDto = PlayerDto.builder()
                .alias("Player1")
                .power(10)
                .name("Name1")
                .lastname("LastName1")
                .dimension("Dimension1")
                .genre(GenderEnum.MALE)
                .build();

        mockMvc.perform(post("/players")
                .header("x-api-key", "apikeytochange")
                .content(objectMapper.writeValueAsString(playerDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(post("/players")
                .header("x-api-key", "apikeytochange")
                .content(objectMapper.writeValueAsString(playerDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict());
    }

    @Test
    public void testPostPlayerWithoutApiKeyReturnForbidden() throws Exception{
        PlayerDto playerDto = PlayerDto.builder()
                .alias("PlayerUnauth")
                .power(10)
                .name("Name1")
                .lastname("LastName1")
                .dimension("Dimension1")
                .genre(GenderEnum.MALE)
                .build();

        mockMvc.perform(post("/players")
                .content(objectMapper.writeValueAsString(playerDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

}
