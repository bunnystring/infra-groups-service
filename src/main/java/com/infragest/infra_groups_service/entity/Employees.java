package com.infragest.infra_groups_service.entity;

import com.infragest.infra_groups_service.enums.EmployeStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un empleado del sistema.
 * Contiene datos personales y el estado del empleado.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "infra_employees")
public class Employees extends BaseEntity{

    /**
     * Nombre completo del empleado.
     */
    @Column(nullable = false)
    private String fullName;

    /**
     * Tipo de documento (ej. CC, CE, PAS).
     */
    @Column(nullable = false)
    private String documentType;

    /**
     * Número del documento de identificación.
     */
    @Column(nullable = false)
    private String documentNumber;

    /**
     * Correo electrónico único del empleado.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Estado del empleado (persistido como STRING).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeStatus status;

}
