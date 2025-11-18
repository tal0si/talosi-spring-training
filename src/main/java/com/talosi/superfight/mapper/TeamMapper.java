package com.talosi.superfight.mapper;

import com.talosi.superfight.dto.PlayerDto;
import com.talosi.superfight.dto.TeamDto;
import com.talosi.superfight.model.PlayerEntity;
import com.talosi.superfight.model.TeamEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TeamMapper {

    public TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    TeamDto map(TeamEntity language);
    TeamEntity map(TeamDto language);

    @Mapping(target="teams", ignore = true)
    PlayerDto map(PlayerEntity player);
    @Mapping(target="teams", ignore = true)
    PlayerEntity map(PlayerDto player);

    List<TeamDto> mapToDto(List<TeamEntity> language);
    List<TeamEntity> mapToEntity(List<TeamDto> language);
}
