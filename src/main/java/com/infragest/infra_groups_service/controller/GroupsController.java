package com.infragest.infra_groups_service.controller;

import com.infragest.infra_groups_service.model.AssignEmployeesRq;
import com.infragest.infra_groups_service.model.GroupMembersEmailRs;
import com.infragest.infra_groups_service.model.GroupRq;
import com.infragest.infra_groups_service.model.GroupRs;
import com.infragest.infra_groups_service.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para gestionar Groups.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
@RestController
@RequestMapping("/groups")
public class GroupsController {

    /**
     * Inyección de la dependencia: groupService
     */
    private final GroupService groupService;

    /**
     * Constructor para la inyección de dependencias.
     * @param groupService
     */
    public GroupsController(GroupService groupService)
    {
        this.groupService = groupService;
    }

    @Operation(summary = "Crear grupo", description = "Crea un nuevo grupo con el payload proporcionado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo creado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupRs.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida / nombre duplicado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del grupo a crear",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupRq.class))
    )
    @PostMapping
    public ResponseEntity<GroupRs> create(@Valid @RequestBody GroupRq dto) {
        return ResponseEntity.ok(groupService.createGroup(dto));
    }

    /**
     * Lista los grupos
     * @return List<GroupRs>
     */
    @Operation(summary = "Listar grupos", description = "Devuelve la lista de todos los grupos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de grupos",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = GroupRs.class))
                    )),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<GroupRs>> listGroups() {
        return ResponseEntity.ok(groupService.listGroups());
    }

    /**
     * Obtiene un grupo por ID.
     */
    @Operation(summary = "Obtener grupo por id", description = "Recupera un grupo por su identificador UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupRs.class))),
            @ApiResponse(responseCode = "400", description = "UUID inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<GroupRs> getGroupById(@PathVariable UUID id) {
        GroupRs dto = groupService.getById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Actualiza un grupo.
     */
    @Operation(summary = "Actualizar grupo", description = "Actualiza los datos de un grupo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Grupo actualizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupRs.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida / nombre duplicado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos a actualizar del grupo",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupRq.class))
    )
    @PutMapping("/{id}")
    public ResponseEntity<GroupRs> update(@PathVariable UUID id, @Valid @RequestBody GroupRq dto) {
        return ResponseEntity.ok(groupService.updateGroup(id, dto));
    }

    /**
     * Elimina un grupo.
     */
    @Operation(summary = "Eliminar grupo", description = "Elimina un grupo por su identificador UUID. No permitido si tiene empleados asociados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Grupo eliminado", content = @Content),
            @ApiResponse(responseCode = "400", description = "UUID inválido / eliminación no permitida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Asigna empleados al grupo.
     *
     * Recibe una lista de employeeIds y devuelve el grupo actualizado.
     */
    @Operation(summary = "Asignar empleados a grupo", description = "Asigna una lista de empleados al grupo indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleados asignados y grupo actualizado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupRs.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida / empleado no encontrado / empleado ya en grupo / empleado inactivo",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Lista de employeeIds a asignar",
            required = true,
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssignEmployeesRq.class))
    )
    @PostMapping("/{id}/employees")
    public ResponseEntity<GroupRs> assignEmployees(@PathVariable("id") UUID id,
                                                   @Valid @RequestBody AssignEmployeesRq rq) {
        return ResponseEntity.ok(groupService.assignEmployees(id, rq));
    }

    /**
     * Remueve un empleado del grupo.
     */
    @Operation(summary = "Remover empleado de grupo", description = "Remueve un empleado del grupo indicado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado removido del grupo", content = @Content),
            @ApiResponse(responseCode = "400", description = "UUID inválido / empleado no perteneciente al grupo / empleado no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}/employees/{employeeId}")
    public ResponseEntity<Void> removeEmployee(@PathVariable("id") UUID id,
                                               @PathVariable("employeeId") UUID employeeId) {
        groupService.removeEmployee(id, employeeId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Devuelve los correos electrónicos de los miembros del grupo.
     * GET /groups/{id}/members/emails
     */
    @Operation(summary = "Obtener emails de los miembros del grupo",
            description = "Devuelve la lista de correos electrónicos de los empleados asignados al grupo indicado. " +
                    "Se eliminan nulos, cadenas vacías y duplicados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de emails del grupo",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GroupMembersEmailRs.class))),
            @ApiResponse(responseCode = "400", description = "UUID inválido o petición inválida",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Grupo no encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/members/emails")
    public ResponseEntity<List<String>> getGroupMembersEmails(@PathVariable UUID id) {
        return ResponseEntity.ok(groupService.getGroupMembersEmails(id));
    }
}
