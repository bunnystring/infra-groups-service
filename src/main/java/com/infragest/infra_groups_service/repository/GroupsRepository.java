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
}
