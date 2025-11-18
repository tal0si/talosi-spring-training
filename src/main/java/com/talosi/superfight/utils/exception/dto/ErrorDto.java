package com.talosi.superfight.utils.exception.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {
    private String status;
    private String errorMessage;
}
