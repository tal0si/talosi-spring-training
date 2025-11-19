package com.talosi.superfight.service;

import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.exception.HttpRequestException;
import com.talosi.superfight.mapper.PlayerMapper;
import com.talosi.superfight.model.GenderEnum;
import com.talosi.superfight.model.PlayerEntity;
import com.talosi.superfight.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {PlayerService.class, PlayerRepository.class})
public class PlayerServiceTest {


    @Autowired
    private PlayerService playerService;

    @MockitoBean
    private PlayerRepository playerRepository;

    private UUID playerId = UUID.randomUUID();

    private final PlayerDto playerDto = PlayerDto.builder()
            .id(playerId)
            .name("TONy")
            .lastname("Stark")
            .alias("Ironman")
            .genre(GenderEnum.MALE)
            .dimension("Marvel")
            .power(123)
            .build();

    private final PlayerEntity playerEntity = PlayerEntity.builder()
            .id(playerId)
            .name("TONy")
            .lastname("Stark")
            .alias("Ironman")
            .genre(GenderEnum.MALE)
            .dimension("Marvel")
            .power(123)
            .build();

    @BeforeEach
    public void init() throws Exception {
        Mockito.when(playerRepository.save(PlayerMapper.INSTANCE.map(playerDto))).thenReturn(playerEntity);
    }

    @Test
    public void testCreation(){
        UUID expected = playerId;
        PlayerDto player = playerService.create(playerDto);

        assertThat(expected).isEqualTo(player.getId());
    }

    @Test
    public void canCreateTwoPlayerWithSameAlias(){
        Mockito.when(playerRepository.findByAlias(playerDto.getAlias())).thenReturn(Optional.of(playerEntity));
        HttpRequestException exception = assertThrows(HttpRequestException.class, () -> {
            playerService.create(playerDto);
        });
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
    }
}
