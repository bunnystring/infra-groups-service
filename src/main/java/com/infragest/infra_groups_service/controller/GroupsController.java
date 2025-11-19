package com.infragest.infra_groups_service.controller;

import com.infragest.infra_groups_service.model.AssignEmployeesRq;
import com.infragest.infra_groups_service.model.GroupRq;
import com.infragest.infra_groups_service.model.GroupRs;
import com.infragest.infra_groups_service.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<GroupRs> create(@Valid @RequestBody GroupRq dto) {
        return ResponseEntity.ok(groupService.createGroup(dto));
    }

    /**
     * Lista los grupos
     * @return
     */
    @GetMapping
    public ResponseEntity<List<GroupRs>> listGroups() {
        List<GroupRs> result = groupService.listGroups();
        return ResponseEntity.ok(result);
    }

    /**
     * Obtiene un grupo por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GroupRs> getGroupById(@PathVariable UUID id) {
        GroupRs dto = groupService.getById(id);
        return ResponseEntity.ok(dto);
    }

    /**
     * Actualiza un grupo.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GroupRs> update(@PathVariable UUID id, @Valid @RequestBody GroupRq dto) {
        GroupRs updated = groupService.updateGroup(id, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * Elimina un grupo.
     */
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
    @PostMapping("/employees/{id}")
    public ResponseEntity<GroupRs> assignEmployees(@PathVariable("id") UUID id,
                                                   @Valid @RequestBody AssignEmployeesRq rq) {
        return ResponseEntity.ok(groupService.assignEmployees(id, rq));
    }

    /**
     * Remueve un empleado del grupo.
     */
    @DeleteMapping("/{id}/employees/{employeeId}")
    public ResponseEntity<Void> removeEmployee(@PathVariable("id") UUID id,
                                               @PathVariable("employeeId") UUID employeeId) {
        groupService.removeEmployee(id, employeeId);
        return ResponseEntity.noContent().build();
    }
}
