package com.talosi.superfight.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@Schema(description = "Team for superfight game")
public class TeamDto {

    @Schema(description = "team id")
    private UUID id;

    @Schema(description = "Team name")
    private String name;

    @Schema(description = "Team members")
    private List<PlayerDto> teamMembers;

}
