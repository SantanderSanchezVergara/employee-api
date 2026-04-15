package com.mutualser.employee.core.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDTO {
    private String code;
    private String message;
}