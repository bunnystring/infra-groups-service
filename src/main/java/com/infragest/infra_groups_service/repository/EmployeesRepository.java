package com.infragest.infra_groups_service.repository;

import com.infragest.infra_groups_service.entity.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la entidad {@link Employees}.
 * Proporciona operaciones CRUD y consultas básicas generadas por Spring Data.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
public interface EmployeesRepository extends JpaRepository<Employees, UUID> {

    /**
     * Comprueba si existe un empleado con el email exacto.
     * @param email email a comprobar
     * @return true si existe al menos un empleado con ese email
     */
    boolean existsByEmail(String email);

    /**
     * Comprueba si existe un empleado con el email ignorando mayúsculas/minúsculas.
     * @param email email a comprobar
     * @return true si existe al menos un empleado con ese email (case-insensitive)
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Obtiene un empleado por su email.
     * @param email email a buscar
     * @return Optional con el empleado si existe
     */
    Optional<Employees> findByEmail(String email);

}
