package com.talosi.superfight.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Player {

    private int id;
    private String name;
    private String lastname;
    private String alias;
    private GenderEnum genre;
    private String dimension;
    private int power;

}
