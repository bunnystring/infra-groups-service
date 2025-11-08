package com.infragest.infra_groups_service.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad que representa un grupo (sede/ubicación) que agrupa empleados.
 *
 * Contiene nombre, dirección y la relación many-to-many con {@link Employees}.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "infra_groups")
public class Group extends BaseEntity{

    /**
     * Nombre del grupo.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Dirección física del grupo.
     */
    @Column(nullable = false)
    private String address;

    /**
     * Empleados asociados al grupo.
     *
     * Relación Many-To-Many mediante la tabla "infra_group_employees".
     */
    @ManyToMany
    @JoinTable(
            name = "infra_group_employees",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "employe_id")
    )
    private Set<Employees> employees = new HashSet<>();

}
