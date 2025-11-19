package com.infragest.infra_groups_service.repository;

import com.infragest.infra_groups_service.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Repositorio JPA para la entidad {@link Group}.
 * Proporciona operaciones CRUD y consultas básicas generadas por Spring Data.
 *
 * @author bunnystring
 * @since 2025-11-07
 */
public interface GroupsRepository extends JpaRepository<Group, UUID> {

    /**
     * Comprueba si existe un grupo con el nombre exactamente igual.
     *
     * @param name nombre a comprobar
     * @return true si existe al menos un Group con ese name
     */
    boolean existsByName(String name);

    /**
     * Comprueba si existe un grupo con el nombre ignorando mayúsculas/minúsculas.
     *
     * @param name nombre a comprobar
     * @return true si existe al menos un Group con ese name (case-insensitive)
     */
    boolean existsByNameIgnoreCase(String name);
}
