package com.mutualser.employee.employee.controller;

import com.mutualser.employee.core.dto.ErrorResponseDTO;
import com.mutualser.employee.employee.dto.EmployeeRequestDTO;
import com.mutualser.employee.employee.dto.EmployeeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Empleado", description = "API para la gestión de empleados")
public interface IEmployeeController {

    @Operation(summary = "Crear un nuevo empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente",
                    content = @Content(schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<EmployeeResponseDTO> save(@Valid @RequestBody EmployeeRequestDTO dto);

    @Operation(summary = "Consultar empleado por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado",
                    content = @Content(schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<EmployeeResponseDTO> findById(@PathVariable Long id);

    @Operation(summary = "Actualizar empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado",
                    content = @Content(schema = @Schema(implementation = EmployeeResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado para actualizar",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<EmployeeResponseDTO> update(@PathVariable Long id, @Valid @RequestBody EmployeeRequestDTO dto);

    @Operation(summary = "Eliminar empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    ResponseEntity<Void> delete(@PathVariable Long id);

    @Operation(summary = "Listar todos los empleados ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeResponseDTO.class)))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<EmployeeResponseDTO>> findAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "30", defaultValue = "30") int size
    );

    @Operation(summary = "Listar empleados igual o mayor a 40 años")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de adultos mayores obtenida",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron empleados en este rango",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<EmployeeResponseDTO>> findSeniors();

    @Operation(summary = "Listar empleadas mujeres")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de mujeres obtenida",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = EmployeeResponseDTO.class)))),
            @ApiResponse(responseCode = "404", description = "No se encontraron empleadas registradas",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    ResponseEntity<List<EmployeeResponseDTO>> findFemales();


}
