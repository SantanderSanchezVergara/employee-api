package com.mutualser.employee.employee.service;

import com.mutualser.employee.employee.dto.EmployeeRequestDTO;
import com.mutualser.employee.employee.dto.EmployeeResponseDTO;
import com.mutualser.employee.employee.mapper.EmployeeMapper;
import com.mutualser.employee.employee.model.Employee;
import com.mutualser.employee.employee.model.enumeration.Gender;
import com.mutualser.employee.employee.repository.EmployeeRepository;
import com.mutualser.employee.employee.service.imp.EmployeeServiceImp;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImpTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImp employeeService;


    private Employee buildEmployee() {
        Employee e = new Employee();
        e.setId(1L);
        e.setFirstName("Juan");
        e.setLastName("Pérez");
        e.setAge(30);
        e.setGender(Gender.MALE);
        e.setEmail("juan@test.com");
        e.setDeleted(false);
        return e;
    }

    private EmployeeRequestDTO buildRequest() {
        return EmployeeRequestDTO.builder()
                .firstName("Juan")
                .lastName("Pérez")
                .age(30)
                .gender(Gender.MALE)
                .email("juan@test.com")
                .build();
    }

    private EmployeeResponseDTO buildResponse() {
        return EmployeeResponseDTO.builder()
                .id(1L)
                .firstName("Juan")
                .lastName("Pérez")
                .age(30)
                .gender(Gender.MALE)
                .email("juan@test.com")
                .build();
    }


    @Test
    void save_cuandoDtoValido_retornaResponseDTO() {
        Employee entity = buildEmployee();
        EmployeeResponseDTO response = buildResponse();
        EmployeeRequestDTO request = buildRequest();

        when(employeeMapper.toEntity(request)).thenReturn(entity);
        when(employeeRepository.save(entity)).thenReturn(entity);
        when(employeeMapper.toResponseDTO(entity)).thenReturn(response);

        EmployeeResponseDTO result = employeeService.save(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Juan");
        verify(employeeRepository, times(1)).save(entity);
    }


    @Test
    void findById_cuandoExisteEmpleado_retornaResponseDTO() {
        Employee entity = buildEmployee();
        EmployeeResponseDTO response = buildResponse();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(employeeMapper.toResponseDTO(entity)).thenReturn(response);

        EmployeeResponseDTO result = employeeService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_cuandoNoExisteEmpleado_lanzaEntityNotFoundException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }


    @Test
    void findAll_cuandoHayEmpleados_retornaLista() {
        Employee entity = buildEmployee();
        EmployeeResponseDTO response = buildResponse();

        when(employeeRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(entity)));
        when(employeeMapper.toResponseDTOList(List.of(entity)))
                .thenReturn(List.of(response));

        List<EmployeeResponseDTO> result = employeeService.findAll(0, 30);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("Juan");
    }

    @Test
    void findAll_cuandoListaVacia_lanzaEntityNotFoundException() {
        when(employeeRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of()));

        assertThatThrownBy(() -> employeeService.findAll(0, 30))
                .isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void findSeniors_cuandoHaySeniors_retornaLista() {
        Employee senior = buildEmployee();
        senior.setAge(45);
        EmployeeResponseDTO response = buildResponse();

        when(employeeRepository.findByAgeGreaterThanEqual(40)).thenReturn(List.of(senior));
        when(employeeMapper.toResponseDTOList(List.of(senior))).thenReturn(List.of(response));

        List<EmployeeResponseDTO> result = employeeService.findSeniors();

        assertThat(result).hasSize(1);
    }

    @Test
    void findSeniors_cuandoNoHaySeniors_lanzaEntityNotFoundException() {
        when(employeeRepository.findByAgeGreaterThanEqual(40)).thenReturn(List.of());

        assertThatThrownBy(() -> employeeService.findSeniors())
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("40");
    }


    @Test
    void findFemales_cuandoHayMujeres_retornaLista() {
        Employee female = buildEmployee();
        female.setGender(Gender.FEMALE);
        EmployeeResponseDTO response = buildResponse();

        when(employeeRepository.findByGender(Gender.FEMALE)).thenReturn(List.of(female));
        when(employeeMapper.toResponseDTOList(List.of(female))).thenReturn(List.of(response));

        List<EmployeeResponseDTO> result = employeeService.findFemales();

        assertThat(result).hasSize(1);
    }

    @Test
    void findFemales_cuandoNoHayMujeres_lanzaEntityNotFoundException() {
        when(employeeRepository.findByGender(Gender.FEMALE)).thenReturn(List.of());

        assertThatThrownBy(() -> employeeService.findFemales())
                .isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void update_cuandoExisteEmpleado_actualizaYRetornaDTO() {
        Employee existing = buildEmployee();
        EmployeeRequestDTO request = buildRequest();
        request.setFirstName("Carlos");
        EmployeeResponseDTO response = buildResponse();
        response.setFirstName("Carlos");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.save(existing)).thenReturn(existing);
        when(employeeMapper.toResponseDTO(existing)).thenReturn(response);

        EmployeeResponseDTO result = employeeService.update(1L, request);

        assertThat(result.getFirstName()).isEqualTo("Carlos");
        verify(employeeRepository).save(existing);
    }

    @Test
    void update_cuandoNoExisteEmpleado_lanzaEntityNotFoundException() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> employeeService.update(99L, buildRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }


    @Test
    void delete_cuandoExisteEmpleado_eliminaCorrectamente() {
        when(employeeRepository.existsById(1L)).thenReturn(true);

        employeeService.delete(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_cuandoNoExisteEmpleado_lanzaEntityNotFoundExceptionYNoElimina() {
        when(employeeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> employeeService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(employeeRepository, never()).deleteById(any());
    }
}
