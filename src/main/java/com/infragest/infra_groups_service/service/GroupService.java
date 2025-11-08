package com.infragest.infra_groups_service.service;

import com.infragest.infra_groups_service.model.AssignEmployeesRq;
import com.infragest.infra_groups_service.model.GroupRq;
import com.infragest.infra_groups_service.model.GroupRs;

import java.util.List;
import java.util.UUID;

/**
 * Servicio para operaciones sobre Group.
 *
 * @author bunnystring
 * @since 2025-11-08
 */
public interface GroupService {

    /**
     * Crea un grupo.
     */
    GroupRs createGroup(GroupRq rq);

    /**
     * Lista todos los grupos.
     */
    List<GroupRs> listGroups();

    /**
     * Obtiene un grupo por ID.
     */
    GroupRs getById(UUID id);

    /**
     * Actualiza un grupo.
     */
    GroupRs updateGroup(UUID id, GroupRq rq);

    /**
     * Elimina un grupo.
     */
    void deleteGroup(UUID id);

    /**
     * Asigna empleados al grupo.
     */
    GroupRs assignEmployees(UUID id, AssignEmployeesRq rq);

    /**
     * Remueve un empleado del grupo.
     */
    void removeEmployee(UUID groupId, UUID employeeId);

}
