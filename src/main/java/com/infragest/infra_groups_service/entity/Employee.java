package com.infragest.infra_groups_service.entity;

import com.infragest.infra_groups_service.enums.EmployeeStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "infra_employees")
public class Employee extends BaseEntity{

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String documentType;

    @Column(nullable = false)
    private String documentNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status;

}
