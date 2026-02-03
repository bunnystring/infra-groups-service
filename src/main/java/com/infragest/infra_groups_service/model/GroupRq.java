package com.infragest.infra_groups_service.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de un Group.
 * Contiene los campos mínimos requeridos.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupRq {

    /**
     * Nombre del grupo.
     */
    @NotBlank(message = "name no puede estar vacío")
    private String name;

    /**
     * Dirección de la oficina donde se encuentra ubicados los equipos.
     */
    @NotBlank(message = "address no puede estar vacío")
    private String address;

}
