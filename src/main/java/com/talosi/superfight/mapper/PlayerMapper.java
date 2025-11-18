package com.talosi.superfight.mapper;

import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.model.PlayerEntity;
import com.talosi.superfight.model.TeamEntity;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PlayerMapper {

    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);


    @Mapping(target="teams", ignore = true)
    PlayerDto map(PlayerEntity player);
    @Mapping(target="teams", ignore = true)
    PlayerEntity map(PlayerDto player);

    @AfterMapping
    default PlayerDto afterMappinDto(PlayerEntity playerEntity,  @MappingTarget PlayerDto playerDto) {
        if (playerEntity.getTeams() != null) {
            playerDto.setTeams(playerEntity.getTeams().stream().map(
                    TeamEntity::getName
            ).toList());
        }
        return playerDto;
    }


    List<PlayerDto> mapToDto(List<PlayerEntity> players);
    List<PlayerEntity> mapToEntity(List<PlayerDto> players);
}
