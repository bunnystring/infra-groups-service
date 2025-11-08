package com.infragest.infra_groups_service.model;

import com.infragest.infra_groups_service.enums.EmployeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO resumido para representar un empleado dentro de la respuesta de Group.
 * Incluye los campos necesarios para listar miembros de un grupo.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSummaryDto {

    /**
     * Identificador único del empleado (UUID).
     */
    private UUID id;

    /**
     * Nombre completo para mostrar en la interfaz.
     */
    private String fullName;

    /**
     * Correo electrónico del empleado.
     */
    private String email;

    /**
     * Estado del empleado (ACTIVE / INACTIVE).
     */
    private EmployeStatus status;
}