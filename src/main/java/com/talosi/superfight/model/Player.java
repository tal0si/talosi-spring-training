package com.talosi.superfight.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Schema(description = "Player entity representing a participant in the game")
public class Player {

    @Schema(description = "Unique identifier of the player", example = "1")
    private int id;

    @Schema(description = "Name of the player", example = "John")
    private String name;


    @Schema(description = "Last name of the player", example = "Doe")
    private String lastname;


    @Schema(description = "Alias of the player", example = "Johnny")
    private String alias;


    @Schema(description = "Gender of the player", example = "MALE")
    private GenderEnum genre;


    @Schema(description = "Dimension of the player", example = "Earth")
    private String dimension;


    @Schema(description = "Power level of the player", example = "100")
    private int power;

}
