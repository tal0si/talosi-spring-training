package com.talosi.superfight.dto;

import lombok.Data;

import java.util.List;

@Data
public class FigthDto {
    private List<String> teams;
    private String winner;

}
