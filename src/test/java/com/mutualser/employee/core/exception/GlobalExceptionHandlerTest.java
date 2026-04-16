package com.mutualser.employee.core.exception;

import com.mutualser.employee.core.dto.ErrorResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleBadCredentials_retorna401ConMensajeFijo() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        ResponseEntity<ErrorResponseDTO> response = handler.handleBadCredentials(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("401");
        assertThat(response.getBody().getMessage()).isEqualTo("Usuario o contraseña incorrectos");
    }

    @Test
    void handleNotFound_retorna404ConMensajeDeLaExcepcion() {
        EntityNotFoundException ex = new EntityNotFoundException("Empleado no encontrado con ID: 5");

        ResponseEntity<ErrorResponseDTO> response = handler.handleNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("404");
        assertThat(response.getBody().getMessage()).isEqualTo("Empleado no encontrado con ID: 5");
    }

    @Test
    void handleValidation_retorna400ConMensajeDelPrimerCampoInvalido() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("employeeRequestDTO", "firstName", "El nombre es obligatorio");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<ErrorResponseDTO> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("100");
        assertThat(response.getBody().getMessage()).isEqualTo("El nombre es obligatorio");
    }
}
