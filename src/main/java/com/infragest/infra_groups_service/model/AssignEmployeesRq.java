package com.infragest.infra_groups_service.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO para asignar varios empleados a un grupo.
 * Contiene la lista de IDs de empleados a asociar.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignEmployeesRq {

    /**
     * Lista de identificadores (UUID) de los empleados a asignar.
     */
    @NotEmpty(message = "employeeIds no puede estar vacío")
    private List<UUID> employeeIds;

}
