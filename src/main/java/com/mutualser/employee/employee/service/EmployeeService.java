package com.mutualser.employee.employee.service;

import com.mutualser.employee.employee.dto.EmployeeRequestDTO;
import com.mutualser.employee.employee.dto.EmployeeResponseDTO;
import java.util.List;

public interface EmployeeService {

    EmployeeResponseDTO save(EmployeeRequestDTO dto);
    EmployeeResponseDTO findById(Long id);
    EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto);
    void delete(Long id);
    List<EmployeeResponseDTO> findAll(int page, int size);
    List<EmployeeResponseDTO> findSeniors();
    List<EmployeeResponseDTO> findFemales();
}
