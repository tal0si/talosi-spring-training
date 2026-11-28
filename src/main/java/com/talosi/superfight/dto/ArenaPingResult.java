package com.talosi.superfight.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArenaPingResult {
    private boolean reachable;
    private Integer httpStatus;
    private Long latencyMs;
    private String error;
}
