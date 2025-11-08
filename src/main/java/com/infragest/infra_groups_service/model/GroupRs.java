package com.infragest.infra_groups_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO de respuesta para un Group.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRs {

    /**
     * Identificador único del grupo (UUID).
     */
    private UUID id;

    /**
     * Nombre descriptivo del grupo.
     */
    private String name;

    /**
     * Dirección física donde se ubica el grupo.
     */
    private String address;

    /**
     * Fecha y hora de creación del registro.
     */
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de la última actualización del registro.
     */
    private LocalDateTime updatedAt;

    /**
     * Empleados asociados al grupo en forma resumida.
     */
    private Set<EmployeeSummaryDto> employees;

}
