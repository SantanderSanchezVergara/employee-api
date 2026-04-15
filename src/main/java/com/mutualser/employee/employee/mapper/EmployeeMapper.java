package com.mutualser.employee.employee.mapper;

import com.mutualser.employee.employee.dto.EmployeeRequestDTO;
import com.mutualser.employee.employee.dto.EmployeeResponseDTO;
import com.mutualser.employee.employee.model.Employee;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    Employee toEntity(EmployeeRequestDTO dto);

    EmployeeResponseDTO toResponseDTO(Employee entity);

    List<EmployeeResponseDTO> toResponseDTOList(List<Employee> entities);
}
