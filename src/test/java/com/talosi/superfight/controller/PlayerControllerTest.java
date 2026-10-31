package com.talosi.superfight.controller;

import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.model.GenderEnum;
import com.talosi.superfight.service.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlayerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlayerService playerService;

    private final PlayerDto playerDto = PlayerDto.builder()
            .id(UUID.randomUUID())
            .name("TONy")
            .lastname("Stark")
            .alias("Ironman")
            .genre(GenderEnum.MALE)
            .dimension("Marvel")
            .power(123)
            .build();

    @Test
    public void testGetPlayer_return0() throws Exception{
        Mockito.when(playerService.getAll()).thenReturn(java.util.Collections.emptyList());
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testGetPlayer_return1() throws Exception{
        Mockito.when(playerService.getAll()).thenReturn(List.of(playerDto));
        mockMvc.perform(get("/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
