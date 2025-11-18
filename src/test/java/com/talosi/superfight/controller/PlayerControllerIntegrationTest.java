package com.talosi.superfight.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.model.GenderEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class PlayerControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    @Test
    public void testGetPlayer() throws Exception{
        mockMvc.perform(get("/players")).andExpect(status().isOk());
    }

    @Test
    public void testPutPlayerWithSameAliasReturnError() throws Exception{
        PlayerDto playerDto = PlayerDto.builder()
                .alias("Player1")
                .power(10)
                .name("Name1")
                .lastname("LastName1")
                .dimension("Dimension1")
                .genre(GenderEnum.MALE)
                .build();

        mockMvc.perform(put("/players")
                .content(objectMapper.writeValueAsString(playerDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        mockMvc.perform(put("/players")
                .content(objectMapper.writeValueAsString(playerDto))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict());
    }

}
