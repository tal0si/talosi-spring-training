package com.talosi.superfight.dto;

import com.talosi.superfight.model.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;


@Data
@AllArgsConstructor
@Schema(description = "Player for superfight game")
public class PlayerDto {

    @Schema(description = "Player id")
    private UUID id;

    @Schema(description = "Player name")
    private String name;

    @Schema(description = "Player lastname")
    private String lastname;

    @Schema(description = "Player alias, it must be unique")
    private String alias;

    @Schema(description = "Player Sex (MALE or FEMALE or OTHER)")
    private GenderEnum genre;

    @Schema(description = "univers where the caracter come from")
    private String dimension;

    @Schema(description = "Power of the player, it must be between 1 and 100")
    private int power;

    private List<String> teams;

}
