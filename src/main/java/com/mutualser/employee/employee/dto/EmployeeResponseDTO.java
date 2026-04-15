package com.mutualser.employee.employee.dto;

import com.mutualser.employee.employee.model.enumeration.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Integer age;
    private String email;
    private LocalDateTime createdAt;
}
