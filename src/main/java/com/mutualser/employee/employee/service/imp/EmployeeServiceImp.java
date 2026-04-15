package com.mutualser.employee.employee.service.imp;

import com.mutualser.employee.employee.dto.EmployeeRequestDTO;
import com.mutualser.employee.employee.dto.EmployeeResponseDTO;
import com.mutualser.employee.employee.mapper.EmployeeMapper;
import com.mutualser.employee.employee.model.Employee;
import com.mutualser.employee.employee.model.enumeration.Gender;
import com.mutualser.employee.employee.repository.EmployeeRepository;
import com.mutualser.employee.employee.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeServiceImp implements EmployeeService {

    private static final int MINIMUM_SENIOR_AGE = 40;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public EmployeeResponseDTO save(EmployeeRequestDTO dto) {
        Employee entity = employeeMapper.toEntity(dto);
        return employeeMapper.toResponseDTO(employeeRepository.save(entity));
    }

    @Override
    public EmployeeResponseDTO findById(Long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + id));
    }

    @Override
    public List<EmployeeResponseDTO> findAll(int page, int size) {
        log.info("Consultando registros de empleados. Límite aplicado: {}", size);

        List<Employee> list = employeeRepository.findAll(
                PageRequest.of(page, size)
        ).getContent();

        if (list.isEmpty()) {
            log.warn("La consulta no retornó resultados");
            throw new EntityNotFoundException("No hay empleados registrados para mostrar");
        }

        return employeeMapper.toResponseDTOList(list);
    }
    @Override
    public List<EmployeeResponseDTO> findSeniors() {

        List<Employee> list = employeeRepository.findByAgeGreaterThanEqual(MINIMUM_SENIOR_AGE);

        if (list.isEmpty()) {
            log.error("No se encontraron resultados para empleados mayores de {}", MINIMUM_SENIOR_AGE);
            throw new EntityNotFoundException("No se encontraron empleados con edad >= " + MINIMUM_SENIOR_AGE);
        }

        log.info("Se encontraron {} empleados senior", list.size());
        return employeeMapper.toResponseDTOList(list);
    }

    @Override
    public List<EmployeeResponseDTO> findFemales() {
        List<Employee> list = employeeRepository.findByGender(Gender.FEMALE);

        if (list.isEmpty()) {
            log.warn("La consulta de género femenino no retornó resultados");
            throw new EntityNotFoundException("No se encontraron registros para el género femenino");
        }

        log.info("Búsqueda exitosa: {} mujeres encontradas", list.size());
        return employeeMapper.toResponseDTOList(list);
    }

    @Override
    public EmployeeResponseDTO update(Long id, EmployeeRequestDTO dto) {
        log.info("Iniciando actualización del empleado ID: {}", id);
        return employeeRepository.findById(id)
                .map(existing -> {
                    log.info("Empleado ID {} localizado, procediendo con el mapeo de datos", id);
                    existing.setFirstName(dto.getFirstName());
                    existing.setLastName(dto.getLastName());
                    existing.setAge(dto.getAge());
                    existing.setGender(dto.getGender());
                    existing.setEmail(dto.getEmail());
                    Employee updated = employeeRepository.save(existing);
                    log.info("Actualización exitosa para el empleado ID: {}", id);
                    return employeeMapper.toResponseDTO(updated);
                })
                .orElseThrow(() -> {
                    log.error("Fallo al actualizar: No existe el empleado con ID: {}", id);
                    return new EntityNotFoundException("No se encontró el empleado para editar");
                });
    }

    @Override
    public void delete(Long id) {
        log.info("Petición de eliminación para el empleado ID: {}", id);
        if (!employeeRepository.existsById(id)) {
            log.error("Imposible eliminar: El ID {} no existe en la base de datos", id);
            throw new EntityNotFoundException("No se puede eliminar un registro inexistente");
        }
        employeeRepository.deleteById(id);
        log.info("Empleado ID: {} eliminado correctamente", id);
    }
}
