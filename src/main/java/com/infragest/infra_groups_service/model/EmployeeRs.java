package com.infragest.infra_groups_service.model;

import com.infragest.infra_groups_service.enums.EmployeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO de respuesta con información completa del empleado.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRs {

    private UUID id;
    private String fullName;
    private String email;
    private EmployeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long version;

}
