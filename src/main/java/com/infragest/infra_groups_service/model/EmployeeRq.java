package com.infragest.infra_groups_service.model;

import com.infragest.infra_groups_service.enums.EmployeStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de request para crear/actualizar un empleado.
 * Ahora usa el enum {@link EmployeStatus} para el campo status.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRq {

    /**
     * Nombre completo del empleado.
     */
    @NotBlank(message = "fullName no puede estar vacío")
    private String fullName;

    /**
     * Correo electrónico del empleado.
     */
    @NotBlank(message = "email no puede estar vacío")
    @Email(message = "email debe tener un formato válido")
    private String email;

    /**
     * Tipo de documento (ej. CC, CE, PAS).
     */
    @NotBlank(message = "documentType no puede estar vacío")
    private String documentType;

    /**
     * Número del documento de identificación.
     */
    @NotBlank(message = "documentNumber no puede estar vacío")
    private String documentNumber;

    /**
     * Estado opcional. Si no se envía, el servicio aplicará por defecto ACTIVE.
     */
    private EmployeStatus status;

}
