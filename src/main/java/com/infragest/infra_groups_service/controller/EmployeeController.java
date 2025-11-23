package com.infragest.infra_groups_service.controller;

import com.infragest.infra_groups_service.model.EmployeeRq;
import com.infragest.infra_groups_service.model.EmployeeRs;
import com.infragest.infra_groups_service.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("employees")
@Tag(name = "Employees", description = "Operaciones para administrar empleados")
public class EmployeeController {

    /**
     * Inyección de la dependencia: EmployeeService.
     */
    private final EmployeeService employeeService;

    /**
     * Constructor para la inyección de dependencias.
     * @param employeeService
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Crea un nuevo empleado.
     *
     * @param rq datos del empleado a crear
     * @return {@link EmployeeRs} del empleado creado
     */
    @Operation(summary = "Crear empleado", description = "Crea un nuevo empleado con la información proporcionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeRs.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida (bad request)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<EmployeeRs> createEmployee(@Valid @RequestBody EmployeeRq rq) {
        return ResponseEntity.ok(employeeService.createEmployee(rq));
    }

    /**
     * Devuelve la lista de todos los empleados.
     *
     * @return lista de {@link EmployeeRs}
     */
    @Operation(summary = "Listar empleados", description = "Devuelve la lista de todos los empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empleados",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EmployeeRs.class))
                    )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<EmployeeRs>> listEmployees() {
        return ResponseEntity.ok(employeeService.listEmployees());
    }

    /**
     * Recupera un empleado por su id.
     *
     * @param id identificador del empleado
     * @return {@link EmployeeRs} si existe
     */
    @Operation(summary = "Obtener empleado por id", description = "Recupera un empleado por su identificador UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeRs.class))),
            @ApiResponse(responseCode = "400", description = "UUID inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRs> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(employeeService.getById(id));
    }

    /**
     * Actualiza un empleado existente.
     *
     * @param id identificador del empleado a actualizar
     * @param rq datos a actualizar
     * @return {@link EmployeeRs} con los datos actualizados
     */
    @Operation(summary = "Actualizar empleado", description = "Actualiza los datos de un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeRs.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida / conflicto de email",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeRs> updateEmployee(@PathVariable UUID id,
                                                     @Valid @RequestBody EmployeeRq rq) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, rq));
    }

    /**
     * Elimina un empleado por ID.
     *
     * @param id identificador del empleado
     * @return ResponseEntity sin contenido
     */
    @Operation(summary = "Eliminar empleado", description = "Elimina un empleado por su identificador UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado", content = @Content),
            @ApiResponse(responseCode = "400", description = "UUID inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

}
